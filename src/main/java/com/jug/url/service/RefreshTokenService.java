package com.jug.url.service;

import com.jug.url.dto.request.CreateRefreshTokenRequest;
import com.jug.url.dto.request.RefreshTokenRequest;
import com.jug.url.dto.response.RefreshTokenResponse;
import com.jug.url.dto.response.ResponseWrapper;

public interface RefreshTokenService {

    String generateRefreshToken(CreateRefreshTokenRequest payload);
    RefreshTokenResponse validateRefreshToken(RefreshTokenRequest payload);
}
