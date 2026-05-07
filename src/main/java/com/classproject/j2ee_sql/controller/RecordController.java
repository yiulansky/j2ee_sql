package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.GameRecord;
import com.classproject.j2ee_sql.mapper.GameRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@Tag(name = "游戏记录", description = "对战记录的增删改查接口")
public class RecordController {

    @Autowired
    private GameRecordMapper recordMapper;

    @Operation(summary = "获取所有对战记录", description = "查询所有游戏对战的记录列表")
    @GetMapping
    public List<GameRecord> listRecords() {
        return recordMapper.selectAllGameRecords();
    }

    @Operation(summary = "保存对战记录", description = "新增一条游戏对战记录")
    @PostMapping
    public void saveRecord(@Parameter(description = "对战记录信息") @RequestBody GameRecord record) {
        recordMapper.insertGameRecord(record);
    }

    @Operation(summary = "更新对战记录", description = "根据ID更新指定的对战记录")
    @PutMapping("/{id}")
    public void updateRecord(@Parameter(description = "记录ID") @PathVariable int id,
                             @Parameter(description = "更新后的对战记录信息") @RequestBody GameRecord record) {
        record.setId(id);
        recordMapper.updateGameRecord(record);
    }

    @Operation(summary = "删除对战记录", description = "根据ID删除指定的对战记录")
    @DeleteMapping("/{id}")
    public void deleteRecord(@Parameter(description = "记录ID") @PathVariable int id) {
        recordMapper.deleteGameRecordById(id);
    }
}
