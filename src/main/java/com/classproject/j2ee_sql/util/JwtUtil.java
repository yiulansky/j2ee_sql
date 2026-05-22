package com.classproject.j2ee_sql.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    // 密钥（至少 256 位，即 32 个字符）
    private static final String SECRET = "j2ee_sql_gobang_secret_key_2026_abcdef123456";
    // 过期时间：7 天
    private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000L;

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成 JWT token
     *
     * @param userId   用户 ID
     * @param username 用户名
     * @return token 字符串
     */
    public static String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析 JWT token，获取 Claims
     *
     * @param token token 字符串
     * @return Claims，解析失败返回 null
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 token 中获取用户 ID
     */
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims != null ? Long.parseLong(claims.getSubject()) : null;
    }

    /**
     * 从 token 中获取用户名
     */
    public static String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get("username", String.class) : null;
    }

    /**
     * 验证 token 是否有效
     */
    public static boolean validateToken(String token) {
        return parseToken(token) != null;
    }
}
