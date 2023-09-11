package Ojt.Board.service;

import Ojt.Board.domain.Board;
import Ojt.Board.domain.Reply;
import Ojt.Board.repository.BoardRepository;
import Ojt.Board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Override
    public void createBoard(Board board) {

        boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Long id, String email) {
        Board board = boardRepository.findById(id).get();
        if (board.getWriterEmail() != email) {
            throw new IllegalStateException("권한이 없습니다");
        }

        boardRepository.deleteById(id);
    }

    @Override
    public Page<Board> getAllBoards(Pageable pageable, String orderCriteria) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, orderCriteria));

        return boardRepository.findAll(pageable);
    }

    @Override
    public Page<Board> getAllBoardsWithKeyword(String keyword, Pageable pageable, String orderCriteria) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, orderCriteria));

        return boardRepository.findByTitleLike("%"+keyword+"%", pageable);
    }

    @Override
    public Board getBoardById(Long id) {


        return boardRepository.findById(id).get();
    }

    @Override
    public Reply getReplyById(Long id) {
        return replyRepository.findById(id).get();
    }

    @Override
    public void editBoard(Long id, Board board) {
        Board toEdit = boardRepository.findById(id).get();
        toEdit.setTitle(board.getTitle());
        toEdit.setContent(board.getContent());
        boardRepository.save(toEdit);
    }

    @Override
    @Transactional
    public void addReply(Long id, Reply reply, String email) {
        Board board = boardRepository.findById(id).get();
        reply.setBoard(board);
        reply.setWriterEmail(email);
        replyRepository.save(reply);
        board.addReply(reply);
    }

    @Override
    @Transactional
    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).get();
        replyRepository.deleteById(replyId);
        reply.getBoard().delReply();
    }

    @Override
    public void editReply(Long replyId, String edit) {
        Reply toEdit = replyRepository.findById(replyId).get();
        toEdit.setRep(edit);
        replyRepository.save(toEdit);
    }


}
