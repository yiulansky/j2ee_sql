package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.GameRecord;
import com.classproject.j2ee_sql.mapper.GameRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/record")
@Tag(name = "游戏记录", description = "对战记录的增删改查接口")
public class RecordController {

    private static final String RECORDS_KEY = "game:records";
    private static final long CACHE_TTL = 60; // 缓存过期时间 60 秒

    @Autowired
    private GameRecordMapper recordMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "获取所有对战记录", description = "查询所有游戏对战的记录列表（Redis 缓存）")
    @GetMapping
    public List<GameRecord> listRecords() {
        // 1. 先查 Redis 缓存
        List<GameRecord> records = (List<GameRecord>) redisTemplate.opsForValue().get(RECORDS_KEY);
        if (records != null) {
            return records;
        }

        // 2. 缓存未命中，查 MySQL
        records = recordMapper.selectAllGameRecords();

        // 3. 回写 Redis 缓存（设置过期时间）
        redisTemplate.opsForValue().set(RECORDS_KEY, records, CACHE_TTL, TimeUnit.SECONDS);

        return records;
    }

    @Operation(summary = "保存对战记录", description = "新增一条游戏对战记录，并清除缓存")
    @PostMapping
    public void saveRecord(@Parameter(description = "对战记录信息") @RequestBody GameRecord record) {
        recordMapper.insertGameRecord(record);
        // 清除缓存，下次查询重新加载
        redisTemplate.delete(RECORDS_KEY);
    }

    @Operation(summary = "更新对战记录", description = "根据ID更新指定的对战记录，并清除缓存")
    @PutMapping("/{id}")
    public void updateRecord(@Parameter(description = "记录ID") @PathVariable int id,
                             @Parameter(description = "更新后的对战记录信息") @RequestBody GameRecord record) {
        record.setId(id);
        recordMapper.updateGameRecord(record);
        // 清除缓存
        redisTemplate.delete(RECORDS_KEY);
    }

    @Operation(summary = "删除对战记录", description = "根据ID删除指定的对战记录，并清除缓存")
    @DeleteMapping("/{id}")
    public void deleteRecord(@Parameter(description = "记录ID") @PathVariable int id) {
        recordMapper.deleteGameRecordById(id);
        // 清除缓存
        redisTemplate.delete(RECORDS_KEY);
    }
}
