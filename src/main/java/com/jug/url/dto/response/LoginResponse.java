package com.jug.url.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private UUID userId;
    private String accessToken;
    private String refreshToken;

}
