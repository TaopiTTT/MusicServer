package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("singer")
public class Singer implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String nickname;

    private String icon;

    private Integer followingsCount;

    private Integer followersCount;

    // MySQL JSON 字段，存储 List<String>
    private Object tags;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    // 获取 tags 列表
    @SuppressWarnings("unchecked")
    public List<String> getTagsList() {
        if (tags == null) {
            return null;
        }
        if (tags instanceof List) {
            return (List<String>) tags;
        }
        return null;
    }
}
