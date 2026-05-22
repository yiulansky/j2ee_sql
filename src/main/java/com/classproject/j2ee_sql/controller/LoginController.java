package com.classproject.j2ee_sql.controller;

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
@Tag(name = "登录管理", description = "用户登录、注册与退出接口")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Operation(summary = "用户登录", description = "根据用户名和密码进行登录认证，登录成功返回 JWT token 和用户信息")
    @PostMapping("/login")
    public Map<String, Object> login(@Parameter(description = "登录请求体，包含 username 和 password")
                                         @RequestBody Map<String, String> loginData) {
        Map<String, Object> result = new HashMap<>();

        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            // 调用 Service 登录，返回包含 token 和用户信息的 Map
            Map<String, Object> loginResult = loginService.login(username, password);

            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("token", loginResult.get("token"));

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", loginResult.get("userId"));
            userMap.put("username", loginResult.get("username"));
            result.put("user", userMap);

        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }

        return result;
    }

    @Operation(summary = "用户注册", description = "注册新用户，用户名不能重复，密码使用 MD5 加密存储")
    @PostMapping("/register")
    public Map<String, Object> register(@Parameter(description = "注册请求体，包含 username 和 password")
                                            @RequestBody Map<String, String> registerData) {
        Map<String, Object> result = new HashMap<>();

        try {
            String username = registerData.get("username");
            String password = registerData.get("password");

            loginService.register(username, password);

            result.put("code", 200);
            result.put("msg", "注册成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }

        return result;
    }

    @Operation(summary = "退出登录", description = "用户退出登录（前端清除 token 即可）")
    @PostMapping("/logout")
    public Map<String, Object> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "退出成功");
        return result;
    }
}
