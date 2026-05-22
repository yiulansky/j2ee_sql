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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/save")
@Tag(name = "游戏存档", description = "保存/读取/加载游戏存档")
public class GameSaveController {

    private static final String SAVES_KEY = "game:saves";
    private static final long CACHE_TTL = 60; // 缓存过期时间 60 秒

    @Autowired
    private GameSaveMapper gameSaveMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ChessState chessState;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Operation(summary = "保存游戏存档", description = "将当前棋盘状态保存为存档，只存储纯棋盘数组")
    @PostMapping("/create")
    public String saveGame(@Parameter(description = "存档名称") @RequestParam String saveName) {
        try {
            // 从 Redis 获取当前棋盘状态
            ChessState currentState = (ChessState) redisTemplate.opsForValue().get("game:chess:state");
            if (currentState == null) {
                currentState = chessState;
            }

            // 只保存 board 二维数组
            String boardJson = objectMapper.writeValueAsString(currentState.getBoard());
            GameSave save = new GameSave(null, saveName, boardJson, LocalDateTime.now());
            gameSaveMapper.insertGameSave(save);

            // 清除存档列表缓存
            redisTemplate.delete(SAVES_KEY);

            return "存档成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "存档失败";
        }
    }

    @Operation(summary = "获取存档列表", description = "获取所有已保存的游戏存档记录（Redis 缓存）")
    @GetMapping("/list")
    public List<GameSave> getSaveList() {
        // 1. 先查 Redis 缓存
        List<GameSave> saves = (List<GameSave>) redisTemplate.opsForValue().get(SAVES_KEY);
        if (saves != null) {
            return saves;
        }

        // 2. 缓存未命中，查 MySQL
        saves = gameSaveMapper.selectAllGameSaves();

        // 3. 回写 Redis 缓存
        redisTemplate.opsForValue().set(SAVES_KEY, saves, CACHE_TTL, TimeUnit.SECONDS);

        return saves;
    }

    @Operation(summary = "加载存档", description = "根据存档ID加载存档，兼容旧的存档数据格式")
    @GetMapping("/load/{id}")
    public String loadGame(@Parameter(description = "存档ID") @PathVariable Integer id) {
        try {
            GameSave save = gameSaveMapper.selectGameSaveById(id);
            String jsonStr = save.getBoardState();

            // 兼容旧数据：判断是纯数组还是对象
            JsonNode root = objectMapper.readTree(jsonStr);
            int[][] board;

            if (root.isObject()) {
                board = objectMapper.treeToValue(root.get("board"), int[][].class);
            } else {
                board = objectMapper.treeToValue(root, int[][].class);
            }

            // 将加载的棋盘状态写入 Redis
            ChessState loadedState = new ChessState();
            loadedState.setBoard(board);
            loadedState.setCurrentPlayer(1);
            loadedState.setGameOver(false);
            loadedState.setWinner(null);
            redisTemplate.opsForValue().set("game:chess:state", loadedState);

            return "加载成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "加载失败";
        }
    }
}
