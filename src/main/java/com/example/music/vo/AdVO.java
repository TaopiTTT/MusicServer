package com.example.music.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AdVO implements Serializable {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String icon;
    private String uri;
    private Integer style;
    private Integer position;
    private Integer sort;
    private Long userId;
}
