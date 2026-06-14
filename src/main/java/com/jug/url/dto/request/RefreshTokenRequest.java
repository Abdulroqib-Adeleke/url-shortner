package com.jug.url.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotNull(message = "please provide a valid refresh token")
    private String refreshToken;

}
