package com.classproject.j2ee_sql.service;

import com.classproject.j2ee_sql.entity.UserAdmin;
import com.classproject.j2ee_sql.mapper.AadminMapper;
import com.classproject.j2ee_sql.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {

    private static final String USER_KEY_PREFIX = "user:info:";
    private static final long USER_CACHE_TTL = 30; // 用户信息缓存 30 分钟

    @Autowired
    private AadminMapper aadminMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录，返回包含 token 和用户信息的 Map
     */
    public Map<String, Object> login(String username, String password) {
        // 1. 先从 Redis 缓存查询用户
        String redisKey = USER_KEY_PREFIX + username;
        UserAdmin userAdmin = (UserAdmin) redisTemplate.opsForValue().get(redisKey);

        // 2. 缓存未命中，查 MySQL
        if (userAdmin == null) {
            userAdmin = aadminMapper.getByUsername(username);
            if (userAdmin != null) {
                // 回写 Redis 缓存（不缓存密码，安全考虑）
                UserAdmin cachedUser = new UserAdmin();
                cachedUser.setId(userAdmin.getId());
                cachedUser.setUsername(userAdmin.getUsername());
                cachedUser.setPassword(userAdmin.getPassword());
                redisTemplate.opsForValue().set(redisKey, cachedUser, USER_CACHE_TTL, TimeUnit.MINUTES);
            }
        }

        // 3. 判断用户是否存在
        if (userAdmin == null) {
            throw new RuntimeException("用户名不存在");
        }

        // 4. MD5 密码比对
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(userAdmin.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 5. 生成 JWT token
        String token = JwtUtil.generateToken(userAdmin.getId(), userAdmin.getUsername());

        // 6. 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", userAdmin.getId());
        result.put("username", userAdmin.getUsername());
        return result;
    }

    /**
     * 用户注册
     */
    public void register(String username, String password) {
        // 1. 检查用户名是否已存在
        UserAdmin existingUser = aadminMapper.getByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. MD5 加密密码
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 3. 插入新用户
        UserAdmin newUser = new UserAdmin();
        newUser.setUsername(username);
        newUser.setPassword(md5Password);
        aadminMapper.insertUser(newUser);
    }
}
