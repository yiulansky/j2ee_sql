package com.classproject.j2ee_sql;

import com.classproject.j2ee_sql.entity.ChessState;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.classproject.j2ee_sql.mapper")
public class J2eeSqlApplication {
    public static void main(String[] args) {
        SpringApplication.run(J2eeSqlApplication.class, args);
    }

    @Bean
    public ChessState chessState() {
        String a;
        return new ChessState();
    }
}