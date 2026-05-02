package com.classproject.j2ee_sql.entity;

import java.time.LocalDateTime;

public class GameSave {
    private Integer id;
    private String saveName;    // 存档名称
    private String boardState; // 棋盘状态（JSON字符串）
    private LocalDateTime saveTime; // 存档时间

    // 无参构造
    public GameSave() {}
    // 全参构造
    public GameSave(Integer id, String saveName, String boardState, LocalDateTime saveTime) {
        this.id = id;
        this.saveName = saveName;
        this.boardState = boardState;
        this.saveTime = saveTime;
    }

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getSaveName() { return saveName; }
    public void setSaveName(String saveName) { this.saveName = saveName; }
    public String getBoardState() { return boardState; }
    public void setBoardState(String boardState) { this.boardState = boardState; }
    public LocalDateTime getSaveTime() { return saveTime; }
    public void setSaveTime(LocalDateTime saveTime) { this.saveTime = saveTime; }
}