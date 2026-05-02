package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.GameRecord;
import com.classproject.j2ee_sql.mapper.GameRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    private GameRecordMapper recordMapper;

    @GetMapping
    public List<GameRecord> listRecords() {
        return recordMapper.selectAllGameRecords();
    }

    @PostMapping
    public void saveRecord(@RequestBody GameRecord record) {
        recordMapper.insertGameRecord(record);
    }

    @PutMapping("/{id}")
    public void updateRecord(@PathVariable int id, @RequestBody GameRecord record) {
        record.setId(id);
        recordMapper.updateGameRecord(record);
    }

    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable int id) {
        recordMapper.deleteGameRecordById(id);
    }
}