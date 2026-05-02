package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.ChessState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private ChessState chessState;

    @GetMapping("/state")
    public ChessState getState() {
        return chessState;
    }

    @PostMapping("/move")
    public ChessState move(@RequestParam int row, @RequestParam int col) {
        if (chessState.isGameOver() || chessState.getBoard()[row][col] != 0) {
            return chessState;
        }

        int currentPlayer = chessState.getCurrentPlayer();
        chessState.getBoard()[row][col] = currentPlayer;

        if (checkWin(row, col, currentPlayer)) {
            chessState.setGameOver(true);
            chessState.setWinner(currentPlayer == 1 ? "黑棋" : "白棋");
        } else {
            chessState.setCurrentPlayer(currentPlayer == 1 ? 2 : 1);
        }
        return chessState;
    }

    @PostMapping("/reset")
    public ChessState reset() {
        chessState.setBoard(new int[15][15]);
        chessState.setCurrentPlayer(1);
        chessState.setGameOver(false);
        chessState.setWinner(null);
        return chessState;
    }

    private boolean checkWin(int r, int c, int player) {
        int[][] dirs = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        for (int[] d : dirs) {
            int count = 1;
            for (int i = 1; i < 5; i++) {
                int nr = r + d[0] * i;
                int nc = c + d[1] * i;
                if (nr < 0 || nr >= 15 || nc < 0 || nc >= 15 || chessState.getBoard()[nr][nc] != player) break;
                count++;
            }
            for (int i = 1; i < 5; i++) {
                int nr = r - d[0] * i;
                int nc = c - d[1] * i;
                if (nr < 0 || nr >= 15 || nc < 0 || nc >= 15 || chessState.getBoard()[nr][nc] != player) break;
                count++;
            }
            if (count >= 5) return true;
        }
        return false;
    }
}