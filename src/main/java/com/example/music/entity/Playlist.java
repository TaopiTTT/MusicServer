package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("playlist")
public class Playlist implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String icon;

    private String detail;

    private Long userId;

    private Integer clicksCount;

    private Integer collectsCount;

    private Integer commentsCount;

    private Integer songsCount;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;
}
