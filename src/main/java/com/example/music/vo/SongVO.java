package com.example.music.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SongVO implements Serializable {
    private String id;
    private String createdAt;
    private String updatedAt;
    private String title;
    private String icon;
    private String uri;
    private Integer clicksCount;
    private Integer commentsCount;
    private String userId;
    private String singerId;
    private UserVO user;
    private SingerVO singer;
}
