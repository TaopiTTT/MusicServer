package com.example.music.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SingerVO implements Serializable {
    private String id;
    private String nickname;
    private String icon;
    private Integer followingsCount;
    private Integer followersCount;
    private List<String> tags;
}
