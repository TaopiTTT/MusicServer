package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("song")
public class Song implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String icon;

    private String uri;

    private Integer clicksCount;

    private Integer commentsCount;

    private Long userId;

    private Long singerId;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    // 非数据库字段，用于关联查询
    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Singer singer;
}
