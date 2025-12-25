package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("playlist_song")
public class PlaylistSong implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long playlistId;

    private Long songId;

    private Integer sortOrder;

    @JsonIgnore
    private LocalDateTime createdAt;
}
