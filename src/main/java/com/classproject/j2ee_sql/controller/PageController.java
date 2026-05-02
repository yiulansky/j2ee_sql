package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.ChessState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private static ChessState chessState = new ChessState();

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("projectName", "五子棋");
        model.addAttribute("boardSize", 15);
        model.addAttribute("chessBoard", chessState.getBoard());
        return "wuziqi";
    }

    public static ChessState getChessState() {
        return chessState;
    }

    public static void setChessState(ChessState state) {
        chessState = state;
    }
}