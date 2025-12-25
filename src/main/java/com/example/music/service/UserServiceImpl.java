package com.example.music.service;

import com.example.music.dto.LoginRequest;
import com.example.music.dto.LoginResponse;
import com.example.music.entity.User;
import com.example.music.mapper.UserMapper;
import com.example.music.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private static final String JWT_SECRET = "your-secret-key-must-be-at-least-256-bits-long-for-hs256-algorithm";
    private static final long JWT_EXPIRATION_DAYS = 7;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getPhone, request.getPhone())
        );

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = generateToken(user);

        LoginResponse.UserVO userVO = new LoginResponse.UserVO(
            user.getId().toString(),
            user.getNickname(),
            user.getIcon()
        );

        return new LoginResponse(token, userVO);
    }

    private String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        Instant now = Instant.now();
        Instant expiration = now.plus(JWT_EXPIRATION_DAYS, ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }

    @Override
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserVO userVO = new UserVO();
        userVO.setId(user.getId().toString());
        userVO.setNickname(user.getNickname());
        userVO.setIcon(user.getIcon());
        userVO.setFollowingsCount(user.getFollowingsCount());
        userVO.setFollowersCount(user.getFollowersCount());
        userVO.setTags(user.getTagsList());
        return userVO;
    }
}
