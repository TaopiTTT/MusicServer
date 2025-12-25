package com.example.music.service;

import com.example.music.dto.LoginRequest;
import com.example.music.dto.LoginResponse;
import com.example.music.vo.UserVO;

public interface UserService {
    LoginResponse login(LoginRequest request);

    UserVO getUserById(Long userId);
}
