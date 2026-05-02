package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.ChessState;
import com.classproject.j2ee_sql.entity.GameSave;
import com.classproject.j2ee_sql.mapper.GameSaveMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/save")
public class GameSaveController {

    @Autowired
    private GameSaveMapper gameSaveMapper;

    @Autowired
    private ChessState chessState;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 保存游戏存档（只存纯棋盘数组）
    @PostMapping("/create")
    public String saveGame(@RequestParam String saveName) {
        try {
            // ✅ 只保存 board 二维数组，不保存整个对象
            String boardJson = objectMapper.writeValueAsString(chessState.getBoard());
            GameSave save = new GameSave(null, saveName, boardJson, LocalDateTime.now());
            gameSaveMapper.insertGameSave(save);
            return "存档成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "存档失败";
        }
    }

    // 获取所有存档
    @GetMapping("/list")
    public List<GameSave> getSaveList() {
        return gameSaveMapper.selectAllGameSaves();
    }

    // 加载存档（兼容旧数据）
    @GetMapping("/load/{id}")
    public String loadGame(@PathVariable Integer id) {
        try {
            GameSave save = gameSaveMapper.selectGameSaveById(id);
            String jsonStr = save.getBoardState();

            // ✅ 兼容旧数据：判断是纯数组还是对象
            JsonNode root = objectMapper.readTree(jsonStr);
            int[][] board;

            if (root.isObject()) {
                // 旧数据：从对象里提取 board
                board = objectMapper.treeToValue(root.get("board"), int[][].class);
            } else {
                // 新数据：直接转数组
                board = objectMapper.treeToValue(root, int[][].class);
            }

            chessState.setBoard(board);
            // 重置游戏状态
            chessState.setCurrentPlayer(1);
            chessState.setGameOver(false);
            chessState.setWinner(null);

            return "加载成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "加载失败";
        }
    }
}
