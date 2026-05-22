package com.classproject.j2ee_sql.mapper;

import com.classproject.j2ee_sql.entity.GameRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GameRecordMapper {


    @Insert("INSERT INTO game_record (player1, player2, winner, duration) VALUES (#{player1}, #{player2}, #{winner}, #{duration})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertGameRecord(GameRecord record);

    @Select("SELECT * FROM game_record ORDER BY id DESC")
    List<GameRecord> selectAllGameRecords();


    @Update("UPDATE game_record SET player1=#{player1}, player2=#{player2}, winner=#{winner}, duration=#{duration} WHERE id=#{id}")
    void updateGameRecord(GameRecord record);

    @Delete("DELETE FROM game_record WHERE id = #{id}")
    void deleteGameRecordById(int id);
}
