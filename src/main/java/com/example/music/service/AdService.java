package com.example.music.service;

import com.example.music.common.PageData;
import com.example.music.vo.AdVO;

public interface AdService {
    PageData<AdVO> getAds(Integer page, Integer size);
}
