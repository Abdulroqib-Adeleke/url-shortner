package com.jug.url.service;

import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.LogoutResponse;
import com.jug.url.dto.response.ResponseWrapper;

import java.util.UUID;

public interface UserService {

    ResponseWrapper<AuthResponse> signupAgent(CreateUserRequest payload);
    ResponseWrapper<AuthResponse> signupSystemAmin(CreateUserRequest payload);
    ResponseWrapper<AuthResponse> signupCustomer(CreateUserRequest payload);
    ResponseWrapper<AuthResponse> login(LoginRequest payload);
    ResponseWrapper<AuthResponse> customerLogin(LoginRequest payload, UUID companyId)
    ResponseWrapper<LogoutResponse> logOut();
}
