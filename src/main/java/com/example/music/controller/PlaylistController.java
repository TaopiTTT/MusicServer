package com.example.music.controller;

import com.example.music.common.PageData;
import com.example.music.common.Result;
import com.example.music.service.PlaylistService;
import com.example.music.vo.PlaylistVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sheets")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    public Result<PageData<PlaylistVO>> getPlaylists(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return playlistService.getPlaylists(page, size);
    }

    @GetMapping("/{id}")
    public Result<PlaylistVO> getPlaylistById(@PathVariable Long id) {
        return playlistService.getPlaylistById(id);
    }
}
