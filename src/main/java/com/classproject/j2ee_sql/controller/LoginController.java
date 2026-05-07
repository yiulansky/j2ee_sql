package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.UserAdmin;
import com.classproject.j2ee_sql.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(tags = "登录接口")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登录接口（保证有返回值，格式和前端完全匹配）
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        Map<String, Object> result = new HashMap<>();

        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            // 调用 Service 登录
            UserAdmin userAdmin = loginService.login(username, password);

            // 登录成功，构造返回结果
            result.put("code", 200);
            result.put("msg", "登录成功");

            // 关键：返回 user 信息，前端要存 localStorage
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", userAdmin.getId());
            userMap.put("username", userAdmin.getUsername());
            result.put("user", userMap);

            // 可选：返回 token（如果不用JWT，返回空字符串也行）
            result.put("token", "");

        } catch (Exception e) {
            // 登录失败
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }

        return result;
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @ApiOperation("退出")
    public Map<String, Object> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "退出成功");
        return result;
    }
}