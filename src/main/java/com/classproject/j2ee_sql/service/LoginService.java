package com.classproject.j2ee_sql.service;

import com.classproject.j2ee_sql.entity.UserAdmin;
import com.classproject.j2ee_sql.mapper.AadminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class LoginService {

    @Autowired
    private AadminMapper aadminMapper;

    public UserAdmin login(String username, String password) {
        // 1. 根据用户名查询用户
        UserAdmin userAdmin = aadminMapper.getByUsername(username);

        // 2. 判断用户是否存在
        if (userAdmin == null) {
            throw new RuntimeException("用户名不存在");
        }

        // 3. MD5 密码比对
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(userAdmin.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return userAdmin;
    }
}
