package com.classproject.j2ee_sql.entity;

public class ChessState {
    private int[][] board = new int[15][15];
    private int currentPlayer = 1;
    private boolean gameOver;
    private String winner;

    public int[][] getBoard() { return board; }
    public void setBoard(int[][] board) { this.board = board; }
    public int getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(int currentPlayer) { this.currentPlayer = currentPlayer; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
}