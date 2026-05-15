package com.jug.url.service.impl;

import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.model.UserModel;
import com.jug.url.repository.UserModelRepository;
import com.jug.url.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserModelRepository userModelRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResponseWrapper<AuthResponse> signup(CreateUserRequest payload) {
        UserModel user = UserModel.builder()
                .name(payload.getName())
                .email(payload.getEmail())
                .password(passwordEncoder.encode(payload.getPassword()))
                .build();
        UserModel savedUser = userModelRepository.save(user);

        return buildAuthResponse(savedUser.getId(), null, "signup successful",
                HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
    }

    @Override
    public ResponseWrapper<AuthResponse> login(LoginRequest payload) {
        return null;
    }

    private ResponseWrapper<AuthResponse> buildAuthResponse(UUID id, String token, String message, HttpStatusCode statusCode){
        AuthResponse response = new AuthResponse(id, token);
        return ResponseWrapper.<AuthResponse>builder()
                .data(response)
                .message(message)
                .statusCode(statusCode)
                .build();
    }
}
