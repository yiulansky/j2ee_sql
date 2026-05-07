package com.classproject.j2ee_sql.controller;

import com.classproject.j2ee_sql.entity.UserAdmin;
import com.classproject.j2ee_sql.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "登录管理", description = "用户登录与退出接口")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Operation(summary = "用户登录", description = "根据用户名和密码进行登录认证，登录成功返回用户信息")
    @PostMapping("/login")
    public Map<String, Object> login(@Parameter(description = "登录请求体，包含 username 和 password")
                                         @RequestBody Map<String, String> loginData) {
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

    @Operation(summary = "退出登录", description = "用户退出登录，清除会话状态")
    @PostMapping("/logout")
    public Map<String, Object> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "退出成功");
        return result;
    }
}
