package com.jug.url.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateRefreshTokenRequest {

    @NotNull(message = "provide a valid user id")
    private UUID userId;
    @NotNull(message = "provide a valid session id")
    private String sessionId;
}
