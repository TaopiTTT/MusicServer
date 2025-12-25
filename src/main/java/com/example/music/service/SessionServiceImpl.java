package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.music.dto.SessionCreateRequest;
import com.example.music.dto.SessionData;
import com.example.music.entity.Session;
import com.example.music.entity.User;
import com.example.music.mapper.SessionMapper;
import com.example.music.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;
    private final UserMapper userMapper;

    private static final String JWT_SECRET = "your-secret-key-must-be-at-least-256-bits-long-for-hs256-algorithm";
    private static final long JWT_EXPIRATION_DAYS = 7;

    @Override
    public SessionData createSession(SessionCreateRequest request) {
        // 1. 验证用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getPhone, request.getPhone())
        );

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 2. 生成会话 token
        String sessionToken = generateToken(user.getId());

        // 3. 生成 chat_token (这里简化处理，实际应该调用融云 API)
        String chatToken = generateChatToken(user.getId());

        // 4. 确定用户角色 (这里简化处理，实际应该从数据库或其他地方获取)
        String roles = "admin,anchor";

        // 5. 创建或更新 session 记录
        Session existingSession = sessionMapper.selectOne(
                new LambdaQueryWrapper<Session>()
                        .eq(Session::getUserId, user.getId())
                        .eq(Session::getDevice, request.getDevice())
        );

        Session session;
        if (existingSession != null) {
            // 更新现有 session
            existingSession.setSessionToken(sessionToken);
            existingSession.setPlatform(request.getPlatform());
            existingSession.setPushToken(request.getPush());
            existingSession.setRoles(roles);
            sessionMapper.updateById(existingSession);
            session = existingSession;
        } else {
            // 创建新 session
            session = new Session();
            session.setUserId(user.getId());
            session.setSessionToken(sessionToken);
            session.setDevice(request.getDevice());
            session.setPlatform(request.getPlatform());
            session.setPushToken(request.getPush());
            session.setRoles(roles);
            sessionMapper.insert(session);
        }

        // 6. 构建响应
        return SessionData.builder()
                .id(session.getId().toString())
                .user_id(user.getId().toString())
                .session(sessionToken)
                .chat_token(chatToken)
                .roles(roles)
                .build();
    }

    private String generateToken(Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        Instant expiration = now.plus(JWT_EXPIRATION_DAYS, ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }

    private String generateChatToken(Long userId) {
        // 简化版本，生成一个假 chat_token
        // 实际项目中应该调用融云 API 获取真实的 token
        String token = UUID.randomUUID().toString().replace("-", "");
        return token + "@" + userId + ".cn.rongnav.com;" + userId + ".cn.rongcfg.com";
    }
}
