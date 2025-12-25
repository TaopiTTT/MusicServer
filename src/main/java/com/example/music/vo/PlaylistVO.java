package com.example.music.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PlaylistVO implements Serializable {
    private String id;
    private String createdAt;
    private String updatedAt;
    private String title;
    private String icon;
    private String detail;
    private String userId;
    private Integer clicksCount;
    private Integer collectsCount;
    private Integer commentsCount;
    private Integer songsCount;

    // 关联数据
    private UserVO user;
    private List<TagVO> tags;
    private List<SongVO> songs;
}
