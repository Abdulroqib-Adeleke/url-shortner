package com.jug.url.service;

import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.LogoutResponse;
import com.jug.url.dto.response.ProfileResponse;
import com.jug.url.dto.response.ResponseWrapper;

public interface UserService {

    ResponseWrapper<AuthResponse> signup(CreateUserRequest payload);
    ResponseWrapper<AuthResponse> login(LoginRequest payload);
    ResponseWrapper<LogoutResponse> logout();
    ResponseWrapper<ProfileResponse> userProfile();
}
