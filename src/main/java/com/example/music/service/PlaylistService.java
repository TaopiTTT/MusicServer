package com.example.music.service;

import com.example.music.common.PageData;
import com.example.music.common.Result;
import com.example.music.vo.PlaylistVO;

public interface PlaylistService {
    Result<PageData<PlaylistVO>> getPlaylists(Integer page, Integer size);
    Result<PlaylistVO> getPlaylistById(Long id);
}
