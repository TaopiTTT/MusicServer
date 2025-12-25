package com.example.music.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Serializable {
    private String token;
    private UserVO user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserVO {
        private String id;
        private String nickname;
        private String icon;
    }
}
