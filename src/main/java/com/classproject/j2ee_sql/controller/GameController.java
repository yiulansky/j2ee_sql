package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.ChessState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@Tag(name = "游戏对战", description = "五子棋游戏核心接口：获取状态、落子、重置")
public class GameController {

    private static final String CHESS_KEY = "game:chess:state";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 从 Redis 获取棋盘状态，如果不存在则创建默认棋盘
     */
    private ChessState getChessState() {
        ChessState state = (ChessState) redisTemplate.opsForValue().get(CHESS_KEY);
        if (state == null) {
            state = new ChessState();
            redisTemplate.opsForValue().set(CHESS_KEY, state);
        }
        return state;
    }

    /**
     * 保存棋盘状态到 Redis
     */
    private void saveChessState(ChessState state) {
        redisTemplate.opsForValue().set(CHESS_KEY, state);
    }

    @Operation(summary = "获取棋盘状态", description = "获取当前棋盘、当前玩家和游戏结束状态")
    @GetMapping("/state")
    public ChessState getState() {
        return getChessState();
    }

    @Operation(summary = "落子", description = "指定行列坐标下棋，自动判断胜负并切换玩家")
    @PostMapping("/move")
    public ChessState move(@Parameter(description = "行坐标（0-14）") @RequestParam int row,
                           @Parameter(description = "列坐标（0-14）") @RequestParam int col) {
        ChessState chessState = getChessState();

        if (chessState.isGameOver() || chessState.getBoard()[row][col] != 0) {
            return chessState;
        }

        int currentPlayer = chessState.getCurrentPlayer();
        chessState.getBoard()[row][col] = currentPlayer;

        if (checkWin(chessState, row, col, currentPlayer)) {
            chessState.setGameOver(true);
            chessState.setWinner(currentPlayer == 1 ? "黑棋" : "白棋");
        } else {
            chessState.setCurrentPlayer(currentPlayer == 1 ? 2 : 1);
        }

        saveChessState(chessState);
        return chessState;
    }

    @Operation(summary = "重置游戏", description = "清空棋盘，重置所有游戏状态为初始值")
    @PostMapping("/reset")
    public ChessState reset() {
        ChessState chessState = new ChessState();
        chessState.setBoard(new int[15][15]);
        chessState.setCurrentPlayer(1);
        chessState.setGameOver(false);
        chessState.setWinner(null);
        saveChessState(chessState);
        return chessState;
    }

    private boolean checkWin(ChessState chessState, int r, int c, int player) {
        int[][] board = chessState.getBoard();
        int[][] dirs = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        for (int[] d : dirs) {
            int count = 1;
            for (int i = 1; i < 5; i++) {
                int nr = r + d[0] * i;
                int nc = c + d[1] * i;
                if (nr < 0 || nr >= 15 || nc < 0 || nc >= 15 || board[nr][nc] != player) break;
                count++;
            }
            for (int i = 1; i < 5; i++) {
                int nr = r - d[0] * i;
                int nc = c - d[1] * i;
                if (nr < 0 || nr >= 15 || nc < 0 || nc >= 15 || board[nr][nc] != player) break;
                count++;
            }
            if (count >= 5) return true;
        }
        return false;
    }
}
