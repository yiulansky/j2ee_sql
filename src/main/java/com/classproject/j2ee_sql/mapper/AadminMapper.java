package com.classproject.j2ee_sql.mapper;

import com.classproject.j2ee_sql.entity.UserAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AadminMapper {
    @Select("select * from user where username = #{username}")
    UserAdmin getByUsername(String username);
}
