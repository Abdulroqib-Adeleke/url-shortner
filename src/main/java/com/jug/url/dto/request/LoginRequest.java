package com.jug.url.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "please provide a valid email")
    @NotNull
    private String email;
    @NotNull(message = "please provide password")
    @NotEmpty(message = "please provide password")
    private String password;

}
