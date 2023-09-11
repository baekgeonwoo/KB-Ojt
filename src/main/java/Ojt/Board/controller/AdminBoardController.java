package Ojt.Board.controller;

import Ojt.Board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AdminBoardController {
    private final BoardService boardService;

    @GetMapping("/admin/delete/{board_id}")
    public String adminDeleteBoard(Model model, @PathVariable("board_id") Long id, Principal principal){
        try{
            boardService.deleteBoard(id, principal.getName());
        }catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/"+String.valueOf(id);
        }

        return "redirect:/";
    }

    @GetMapping("/admin/delete/{board_id}/reply/{reply_id}")
    public String adminDeleteReply(Model model, @PathVariable("board_id") Long id,  @PathVariable("reply_id") Long replyId, Principal principal){
        boardService.deleteReply(replyId);

        return "redirect:/"+String.valueOf(id);
    }
}
