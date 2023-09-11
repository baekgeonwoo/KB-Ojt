package Ojt.Board.service;


import Ojt.Board.domain.Board;
import Ojt.Board.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    void createBoard(Board board);
    void deleteBoard(Long id, String email);
    Page<Board> getAllBoards(Pageable pageable, String orderCriteria);

    Page<Board> getAllBoardsWithKeyword(String keyword, Pageable pageable, String orderCriteria);

    Board getBoardById(Long id);

    Reply getReplyById(Long id);

    void editBoard(Long id, Board board);

    void addReply(Long id, Reply reply, String email);

    void deleteReply(Long replyId);

    void editReply(Long replyId, String edit);
}
