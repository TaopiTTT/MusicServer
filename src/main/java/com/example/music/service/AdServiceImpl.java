package com.example.music.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.common.PageData;
import com.example.music.entity.Ad;
import com.example.music.mapper.AdMapper;
import com.example.music.vo.AdVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdMapper adMapper;

    @Override
    public PageData<AdVO> getAds(Integer page, Integer size) {
        IPage<Ad> pageParam = new Page<>(page, size);
        IPage<Ad> resultPage = adMapper.selectPage(pageParam, null);

        PageData<AdVO> pageData = new PageData<>();
        pageData.setTotal(resultPage.getTotal());
        pageData.setPages((int) resultPage.getPages());
        pageData.setSize((int) resultPage.getSize());
        pageData.setPage((int) resultPage.getCurrent());
        pageData.setData(convertToVOList(resultPage.getRecords()));

        return pageData;
    }

    private List<AdVO> convertToVOList(List<Ad> ads) {
        return ads.stream().map(ad -> {
            AdVO vo = new AdVO();
            vo.setId(ad.getId().toString());
            vo.setCreatedAt(ad.getCreatedAt());
            vo.setUpdatedAt(ad.getUpdatedAt());
            vo.setTitle(ad.getTitle());
            vo.setIcon(ad.getIcon());
            vo.setUri(ad.getUri());
            vo.setStyle(ad.getStyle());
            vo.setPosition(ad.getPosition());
            vo.setSort(ad.getSort());
            vo.setUserId(ad.getUserId());
            return vo;
        }).toList();
    }
}
