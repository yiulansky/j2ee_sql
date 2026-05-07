package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.ChessState;
import com.classproject.j2ee_sql.entity.GameSave;
import com.classproject.j2ee_sql.mapper.GameSaveMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/save")
@Tag(name = "游戏存档", description = "保存/读取/加载游戏存档")
public class GameSaveController {

    @Autowired
    private GameSaveMapper gameSaveMapper;

    @Autowired
    private ChessState chessState;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Operation(summary = "保存游戏存档", description = "将当前棋盘状态保存为存档，只存储纯棋盘数组")
    @PostMapping("/create")
    public String saveGame(@Parameter(description = "存档名称") @RequestParam String saveName) {
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

    @Operation(summary = "获取存档列表", description = "获取所有已保存的游戏存档记录")
    @GetMapping("/list")
    public List<GameSave> getSaveList() {
        return gameSaveMapper.selectAllGameSaves();
    }

    @Operation(summary = "加载存档", description = "根据存档ID加载存档，兼容旧的存档数据格式")
    @GetMapping("/load/{id}")
    public String loadGame(@Parameter(description = "存档ID") @PathVariable Integer id) {
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
