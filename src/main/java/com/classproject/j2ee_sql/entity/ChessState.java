package com.classproject.j2ee_sql.entity;

public class ChessState {
    private int[][] board = new int[15][15];
    private int currentPlayer = 1;
    private boolean gameOver;
    private String winner;
    /** 游戏开始时间戳（毫秒），用于计时 */
    private Long startTime;
    /** 已用秒数（用于恢复存档时） */
    private int elapsedSeconds;

    public int[][] getBoard() { return board; }
    public void setBoard(int[][] board) { this.board = board; }
    public int getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(int currentPlayer) { this.currentPlayer = currentPlayer; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
    public Long getStartTime() { return startTime; }
    public void setStartTime(Long startTime) { this.startTime = startTime; }
    public int getElapsedSeconds() { return elapsedSeconds; }
    public void setElapsedSeconds(int elapsedSeconds) { this.elapsedSeconds = elapsedSeconds; }
}
