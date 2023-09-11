package Ojt.Board.controller;

import Ojt.Board.domain.Board;
import Ojt.Board.domain.Reply;
import Ojt.Board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/")
    public String boardList(Model model, @PageableDefault(page=0, size=9, sort="id", direction = Sort.Direction.DESC)Pageable pageable, String keyword, @RequestParam(required=false, defaultValue = "id", value="orderby")String orderCriteria ){
        Page<Board> boardList ;
        if(keyword == null){
            boardList = boardService.getAllBoards(pageable, orderCriteria) ;
        }else{
            boardList = boardService.getAllBoardsWithKeyword(keyword, pageable, orderCriteria) ;
        }
        int curPage = boardList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(curPage - 5, 1);
        int endPage = Math.min(curPage + 5, boardList.getTotalPages()) ;
        if (endPage == 0 ) endPage = 1;

        model.addAttribute("boards", boardList);
        model.addAttribute("curPage", curPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPage", boardList.getTotalPages());
        model.addAttribute("keyword", keyword);
        return "boardList";
    }

    @GetMapping("/{board_id}")
    public String getBoard(Model model, @PathVariable("board_id") Long id) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        model.addAttribute("reply",new Reply());

        return "board";
    }


    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("board", new Board());
        return "addBoard";
    }

    @PostMapping("/create")
    public String createBoard(@Valid Board board, BindingResult result, Model model, Principal principal){
        if(result.hasErrors()){
            return "addBoard";
        }
        board.setWriterEmail(principal.getName());
        boardService.createBoard(board);
        return "redirect:/"+String.valueOf(board.getId());
    }

    @GetMapping("/delete/{board_id}")
    public String deleteBoard(Model model, @PathVariable("board_id") Long id, Principal principal){
        if(principal == null){
            model.addAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/"+String.valueOf(id);
        }

        try{
            boardService.deleteBoard(id, principal.getName());
        }catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/"+String.valueOf(id);
        }

        return "redirect:/";
    }

    @GetMapping("/edit/{board_id}")
    public String editForm(Model model, @PathVariable("board_id") Long id, Principal principal){
        Board board = boardService.getBoardById(id);

        if(principal == null || board.getWriterEmail() != principal.getName()){
            model.addAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/"+String.valueOf(id);
        }


        model.addAttribute("board", board);

        return "editBoard";
    }

    @PostMapping("/edit/{board_id}")
    public String editBoard(@PathVariable("board_id") Long id, @Valid Board board, BindingResult result){
        if(result.hasErrors()){
            return "editBoard";
        }

        boardService.editBoard(id, board);

        return "redirect:/"+String.valueOf(id);
    }

    @PostMapping("/create/{board_id}/reply")
    public String addReply(@PathVariable("board_id") Long id, @Valid Reply reply, BindingResult result, Model model, Principal principal){
        if(result.hasErrors()){
            model.addAttribute("board", boardService.getBoardById(id));
            return "board";
        }
        if (principal == null) {
            model.addAttribute("errorMessage", "비회원은 댓글을 작성할 수 없습니다.");
            return "redirect:/"+String.valueOf(id);
        }

        boardService.addReply(id, reply, principal.getName());
        return "redirect:/"+String.valueOf(id);
    }

    @GetMapping("/delete/{board_id}/reply/{reply_id}")
    public String deleteReply(Model model, @PathVariable("board_id") Long id,  @PathVariable("reply_id") Long replyId, Principal principal){
        if (principal == null || boardService.getReplyById(replyId).getWriterEmail() != principal.getName()) {
            model.addAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/"+String.valueOf(id);
        }

        boardService.deleteReply(replyId);

        return "redirect:/"+String.valueOf(id);
    }

    @GetMapping("/edit/{board_id}/reply/{reply_id}")
    public String editReply(@PathVariable("board_id") Long id, @PathVariable("reply_id") Long replyId, Model model, @RequestParam("edit") String edit, Principal principal){
        if (principal == null || boardService.getReplyById(replyId).getWriterEmail() != principal.getName()) {
            model.addAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/"+String.valueOf(id);
        }

        boardService.editReply(replyId, edit);

        model.addAttribute("board", boardService.getBoardById(id));
        model.addAttribute("reply",new Reply());
        return "redirect:/"+String.valueOf(id);
    }
}
