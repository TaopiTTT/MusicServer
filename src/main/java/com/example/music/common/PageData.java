package com.example.music.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageData<T> implements Serializable {
    private Long total;
    private Integer pages;
    private Integer size;
    private Integer page;
    private Integer next;  // 仅歌单接口需要
    private List<T> data;
}
