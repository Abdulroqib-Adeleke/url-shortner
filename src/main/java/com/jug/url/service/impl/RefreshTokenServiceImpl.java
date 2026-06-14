package com.jug.url.service.impl;

import com.jug.url.auth.JwtService;
import com.jug.url.dto.request.CreateRefreshTokenRequest;
import com.jug.url.dto.request.RefreshTokenRequest;
import com.jug.url.dto.response.RefreshTokenResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.exceptions.BadRequestException;
import com.jug.url.exceptions.ResourceNotFoundException;
import com.jug.url.model.RefreshToken;
import com.jug.url.repository.RefreshTokenRepository;
import com.jug.url.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public String generateRefreshToken(CreateRefreshTokenRequest payload) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUserId(payload.getUserId());
        if(refreshTokenOptional.isPresent())
            refreshTokenRepository.deleteByUserId(payload.getUserId());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(jwtService.generateRefreshToken())
                .userId(payload.getUserId())
                .sessionId(payload.getSessionId())
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    @Override
    public RefreshTokenResponse validateRefreshToken(RefreshTokenRequest token) {

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token.getRefreshToken());

        if(refreshTokenOptional.isEmpty()) throw new BadRequestException("Invalid refresh token");

        if(refreshTokenOptional.get().getExpiryDate().isBefore(Instant.now())) throw new RuntimeException("Refresh token expired");

        return new RefreshTokenResponse(refreshTokenOptional.get().getToken());
    }
}
