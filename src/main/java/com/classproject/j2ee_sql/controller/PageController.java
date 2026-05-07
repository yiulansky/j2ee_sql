package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.ChessState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "页面跳转", description = "前端页面路由接口")
public class PageController {

    private static ChessState chessState = new ChessState();

    @Operation(summary = "进入游戏页面", description = "访问五子棋游戏首页，传入棋盘数据")
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("projectName", "五子棋");
        model.addAttribute("boardSize", 15);
        model.addAttribute("chessBoard", chessState.getBoard());
        return "wuziqi";
    }

    @Operation(summary = "进入登录页面", description = "访问用户登录页面")
    @GetMapping({"/login.html", "/login"})
    public String loginPage() {
        // 注意：这里返回 "login"，对应 templates/login.html
        return "login";
    }

    public static ChessState getChessState() {
        return chessState;
    }

    public static void setChessState(ChessState state) {
        chessState = state;
    }
}
