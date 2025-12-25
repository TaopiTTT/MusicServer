package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("ad")
public class Ad implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String icon;

    private String uri;

    private Integer style;

    private Integer position;

    private Integer sort;

    private Long userId;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;
}
