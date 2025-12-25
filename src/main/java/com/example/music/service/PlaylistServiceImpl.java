package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.common.PageData;
import com.example.music.common.Result;
import com.example.music.entity.*;
import com.example.music.mapper.*;
import com.example.music.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistMapper playlistMapper;
    private final UserMapper userMapper;
    private final PlaylistTagMapper playlistTagMapper;
    private final TagMapper tagMapper;
    private final PlaylistSongMapper playlistSongMapper;
    private final SongMapper songMapper;
    private final SingerMapper singerMapper;

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public Result<PageData<PlaylistVO>> getPlaylists(Integer page, Integer size) {
        // 分页查询
        Page<Playlist> playlistPage = new Page<>(page, size);
        IPage<Playlist> playlistIPage = playlistMapper.selectPage(playlistPage, null);

        // 转换为 VO
        List<PlaylistVO> playlistVOs = new ArrayList<>();
        for (Playlist playlist : playlistIPage.getRecords()) {
            PlaylistVO vo = convertToVO(playlist);
            playlistVOs.add(vo);
        }

        // 构建分页数据
        PageData<PlaylistVO> pageData = new PageData<>();
        pageData.setTotal(playlistIPage.getTotal());
        pageData.setPages((int) playlistIPage.getPages());
        pageData.setSize((int) playlistIPage.getSize());
        pageData.setPage((int) playlistIPage.getCurrent());

        // 计算 next 字段: next = page < pages ? page + 1 : null
        int currentPage = (int) playlistIPage.getCurrent();
        int totalPages = (int) playlistIPage.getPages();
        if (currentPage < totalPages) {
            pageData.setNext(currentPage + 1);
        } else {
            pageData.setNext(null);
        }

        pageData.setData(playlistVOs);

        return Result.success(pageData);
    }

    @Override
    public Result<PlaylistVO> getPlaylistById(Long id) {
        Playlist playlist = playlistMapper.selectById(id);
        if (playlist == null) {
            Result<PlaylistVO> result = new Result<>();
            result.setStatus(404);
            result.setData(null);
            return result;
        }
        return Result.success(convertToVOWithDetails(playlist));
    }

    private PlaylistVO convertToVO(Playlist playlist) {
        PlaylistVO vo = new PlaylistVO();
        vo.setId(String.valueOf(playlist.getId()));
        vo.setTitle(playlist.getTitle());
        vo.setIcon(playlist.getIcon());
        vo.setDetail(playlist.getDetail());
        vo.setUserId(String.valueOf(playlist.getUserId()));
        vo.setClicksCount(playlist.getClicksCount());
        vo.setCollectsCount(playlist.getCollectsCount());
        vo.setCommentsCount(playlist.getCommentsCount());
        vo.setSongsCount(playlist.getSongsCount());

        if (playlist.getCreatedAt() != null) {
            vo.setCreatedAt(playlist.getCreatedAt().format(ISO_FORMATTER));
        }
        if (playlist.getUpdatedAt() != null) {
            vo.setUpdatedAt(playlist.getUpdatedAt().format(ISO_FORMATTER));
        }

        return vo;
    }

    private PlaylistVO convertToVOWithDetails(Playlist playlist) {
        PlaylistVO vo = convertToVO(playlist);

        // 填充用户信息
        if (playlist.getUserId() != null) {
            User user = userMapper.selectById(playlist.getUserId());
            if (user != null) {
                vo.setUser(convertUserToVO(user));
            }
        }

        // 填充标签信息
        LambdaQueryWrapper<PlaylistTag> tagWrapper = new LambdaQueryWrapper<>();
        tagWrapper.eq(PlaylistTag::getPlaylistId, playlist.getId());
        List<PlaylistTag> playlistTags = playlistTagMapper.selectList(tagWrapper);

        if (!playlistTags.isEmpty()) {
            List<Long> tagIds = playlistTags.stream()
                    .map(PlaylistTag::getTagId)
                    .collect(Collectors.toList());

            List<Tag> tags = tagMapper.selectBatchIds(tagIds);
            List<TagVO> tagVOs = tags.stream()
                    .map(this::convertTagToVO)
                    .collect(Collectors.toList());
            vo.setTags(tagVOs);
        }

        // 填充歌曲信息
        LambdaQueryWrapper<PlaylistSong> songWrapper = new LambdaQueryWrapper<>();
        songWrapper.eq(PlaylistSong::getPlaylistId, playlist.getId())
                .orderByAsc(PlaylistSong::getSortOrder);
        List<PlaylistSong> playlistSongs = playlistSongMapper.selectList(songWrapper);

        if (!playlistSongs.isEmpty()) {
            List<Long> songIds = playlistSongs.stream()
                    .map(PlaylistSong::getSongId)
                    .collect(Collectors.toList());

            List<Song> songs = songMapper.selectBatchIds(songIds);
            List<SongVO> songVOs = songs.stream()
                    .map(this::convertSongToVO)
                    .collect(Collectors.toList());
            vo.setSongs(songVOs);
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

    private TagVO convertTagToVO(Tag tag) {
        TagVO vo = new TagVO();
        vo.setId(String.valueOf(tag.getId()));
        vo.setTitle(tag.getTitle());
        return vo;
    }

    private SongVO convertSongToVO(Song song) {
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

        // 填充User信息
        if (song.getUserId() != null) {
            User user = userMapper.selectById(song.getUserId());
            if (user != null) {
                vo.setUser(convertUserToVO(user));
            }
        }

        // 填充Singer信息
        if (song.getSingerId() != null) {
            Singer singer = singerMapper.selectById(song.getSingerId());
            if (singer != null) {
                vo.setSinger(convertSingerToVO(singer));
            }
        }

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
