package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.common.PageData;
import com.example.music.common.Result;
import com.example.music.entity.Singer;
import com.example.music.entity.Song;
import com.example.music.entity.User;
import com.example.music.mapper.SingerMapper;
import com.example.music.mapper.SongMapper;
import com.example.music.mapper.UserMapper;
import com.example.music.vo.SingerVO;
import com.example.music.vo.SongVO;
import com.example.music.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongMapper songMapper;
    private final UserMapper userMapper;
    private final SingerMapper singerMapper;

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public Result<PageData<SongVO>> getSongs(Integer page, Integer size) {
        // 分页查询
        Page<Song> songPage = new Page<>(page, size);
        IPage<Song> songIPage = songMapper.selectPage(songPage, null);

        // 转换为 VO
        List<SongVO> songVOs = new ArrayList<>();
        for (Song song : songIPage.getRecords()) {
            SongVO vo = convertToVO(song);
            songVOs.add(vo);
        }

        // 构建分页数据
        PageData<SongVO> pageData = new PageData<>();
        pageData.setTotal(songIPage.getTotal());
        pageData.setPages((int) songIPage.getPages());
        pageData.setSize((int) songIPage.getSize());
        pageData.setPage((int) songIPage.getCurrent());
        pageData.setData(songVOs);

        return Result.success(pageData);
    }

    private SongVO convertToVO(Song song) {
        SongVO vo = new SongVO();
        vo.setId(String.valueOf(song.getId()));
        vo.setTitle(song.getTitle());
        vo.setIcon(song.getIcon());
        vo.setUri(song.getUri());
        vo.setClicksCount(song.getClicksCount());
        vo.setCommentsCount(song.getCommentsCount());
        vo.setUserId(String.valueOf(song.getUserId()));
        vo.setSingerId(String.valueOf(song.getSingerId()));

        if (song.getCreatedAt() != null) {
            vo.setCreatedAt(song.getCreatedAt().format(ISO_FORMATTER));
        }
        if (song.getUpdatedAt() != null) {
            vo.setUpdatedAt(song.getUpdatedAt().format(ISO_FORMATTER));
        }

        // 填充 User 信息
        if (song.getUser() != null) {
            vo.setUser(convertUserToVO(song.getUser()));
        } else if (song.getUserId() != null) {
            User user = userMapper.selectById(song.getUserId());
            if (user != null) {
                vo.setUser(convertUserToVO(user));
            }
        }

        // 填充 Singer 信息
        if (song.getSinger() != null) {
            vo.setSinger(convertSingerToVO(song.getSinger()));
        } else if (song.getSingerId() != null) {
            Singer singer = singerMapper.selectById(song.getSingerId());
            if (singer != null) {
                vo.setSinger(convertSingerToVO(singer));
            }
        }

        return vo;
    }

    private UserVO convertUserToVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(String.valueOf(user.getId()));
        vo.setNickname(user.getNickname());
        vo.setIcon(user.getIcon());
        vo.setFollowingsCount(user.getFollowingsCount());
        vo.setFollowersCount(user.getFollowersCount());
        vo.setTags(user.getTagsList());
        return vo;
    }

    private SingerVO convertSingerToVO(Singer singer) {
        SingerVO vo = new SingerVO();
        vo.setId(String.valueOf(singer.getId()));
        vo.setNickname(singer.getNickname());
        vo.setIcon(singer.getIcon());
        vo.setFollowingsCount(singer.getFollowingsCount());
        vo.setFollowersCount(singer.getFollowersCount());
        vo.setTags(singer.getTagsList());
        return vo;
    }
}
