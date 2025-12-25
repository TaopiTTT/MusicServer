package com.example.music.service;

import com.example.music.dto.SessionCreateRequest;
import com.example.music.dto.SessionData;

public interface SessionService {
    SessionData createSession(SessionCreateRequest request);
}
