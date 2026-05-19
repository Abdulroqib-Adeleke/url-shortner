package com.jug.url.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserLoginSessionService {
    void createLoginSession(String sessionId, UUID userId);
    void invalidateLoginSession(UUID userId);
    String getUserSession(UUID userId);
}
