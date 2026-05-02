package com.classproject.j2ee_sql.mapper;

import com.classproject.j2ee_sql.entity.GameSave;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 棋盘存档 Mapper 接口
 */
@Mapper
public interface GameSaveMapper {

    /**
     * 保存棋盘存档
     */
    @Insert("INSERT INTO game_save (save_name, board_state, save_time) VALUES (#{saveName}, #{boardState}, #{saveTime})")
    void insertGameSave(GameSave save);

    /**
     * 查询所有存档
     */
    @Select("SELECT * FROM game_save ORDER BY save_time DESC")
    List<GameSave> selectAllGameSaves();

    /**
     * 根据 ID 查询存档
     */
    @Select("SELECT * FROM game_save WHERE id = #{id}")
    GameSave selectGameSaveById(int id);
}