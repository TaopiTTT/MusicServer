package com.example.music.controller;

import com.example.music.common.Result;
import com.example.music.dto.SessionCreateRequest;
import com.example.music.dto.SessionData;
import com.example.music.dto.SessionResponse;
import com.example.music.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    /**
     * POST /v1/sessions - Creates a new session (Login)
     */
    @PostMapping
    public Result<SessionData> createSession(@Valid @RequestBody SessionCreateRequest request) {
        return Result.success(sessionService.createSession(request));
    }

    /**
     * GET /v1/sessions - Returns session information (200 OK)
     */
    @GetMapping
    public Result<SessionResponse> getSession(HttpServletRequest request) {
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        SessionResponse response = SessionResponse.builder()
                .sessionId(sessionId)
                .status("active")
                .createdAt(now)
                .expiresAt(now.plusHours(24))
                .isActive(true)
                .build();

        return Result.success(response);
    }

    /**
     * GET /v1/sessions/{id} - Returns session by ID (200 OK)
     */
    @GetMapping("/{id}")
    public Result<SessionResponse> getSessionById(@PathVariable String id) {
        LocalDateTime now = LocalDateTime.now();

        SessionResponse response = SessionResponse.builder()
                .sessionId(id)
                .status("active")
                .createdAt(now.minusHours(1))
                .expiresAt(now.plusHours(23))
                .isActive(true)
                .build();

        return Result.success(response);
    }

    /**
     * PUT /v1/sessions/{id} - Updates session (200 OK)
     */
    @PutMapping("/{id}")
    public Result<SessionResponse> updateSession(@PathVariable String id) {
        LocalDateTime now = LocalDateTime.now();

        SessionResponse response = SessionResponse.builder()
                .sessionId(id)
                .status("updated")
                .createdAt(now.minusHours(1))
                .expiresAt(now.plusHours(24))
                .isActive(true)
                .build();

        return Result.success(response);
    }

    /**
     * DELETE /v1/sessions/{id} - Deletes session (204 No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable String id) {
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /v1/sessions/bad-request - Demonstrates 400 Bad Request
     */
    @GetMapping("/bad-request")
    public ResponseEntity<Map<String, String>> badRequest() {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("message", "Invalid request parameters");
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * GET /v1/sessions/unauthorized - Demonstrates 401 Unauthorized
     */
    @GetMapping("/unauthorized")
    public ResponseEntity<Map<String, String>> unauthorized() {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Unauthorized");
        error.put("message", "Authentication required");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * GET /v1/sessions/forbidden - Demonstrates 403 Forbidden
     */
    @GetMapping("/forbidden")
    public ResponseEntity<Map<String, String>> forbidden() {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Forbidden");
        error.put("message", "Access denied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * GET /v1/sessions/not-found - Demonstrates 404 Not Found
     */
    @GetMapping("/not-found")
    public ResponseEntity<Map<String, String>> notFound() {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Not Found");
        error.put("message", "Resource not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * GET /v1/sessions/error - Demonstrates 500 Internal Server Error
     */
    @GetMapping("/error")
    public ResponseEntity<Map<String, String>> internalError() {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
