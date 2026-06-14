package com.jug.url.service;

import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.request.RefreshTokenRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.LoginResponse;
import com.jug.url.dto.response.LogoutResponse;
import com.jug.url.dto.response.ResponseWrapper;

import java.util.UUID;

public interface UserService {

    ResponseWrapper<AuthResponse> signupAgent(CreateUserRequest payload);
    ResponseWrapper<AuthResponse> signupSystemAdmin(CreateUserRequest payload);
    ResponseWrapper<AuthResponse> signupCustomer(CreateUserRequest payload);
    ResponseWrapper<LoginResponse> login(LoginRequest payload);
    ResponseWrapper<LoginResponse> customerLogin(LoginRequest payload, UUID companyId);
    ResponseWrapper<LoginResponse> refreshToken(RefreshTokenRequest payload);
    ResponseWrapper<LogoutResponse> logOut();
}
