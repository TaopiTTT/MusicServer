package com.example.music.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.common.PageData;
import com.example.music.common.Result;
import com.example.music.vo.SongVO;

public interface SongService {
    Result<PageData<SongVO>> getSongs(Integer page, Integer size);
}
