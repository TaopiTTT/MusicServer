package com.example.music.controller;

import com.example.music.common.Result;
import com.example.music.dto.LoginRequest;
import com.example.music.dto.LoginResponse;
import com.example.music.service.UserService;
import com.example.music.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @GetMapping("/{userId}")
    public Result<UserVO> getUserById(@PathVariable Long userId) {
        return Result.success(userService.getUserById(userId));
    }
}
