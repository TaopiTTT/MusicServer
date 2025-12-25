package com.example.music.controller;

import com.example.music.common.PageData;
import com.example.music.common.Result;
import com.example.music.service.SongService;
import com.example.music.vo.SongVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping
    public Result<PageData<SongVO>> getSongs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return songService.getSongs(page, size);
    }
}
