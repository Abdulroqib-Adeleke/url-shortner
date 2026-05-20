package com.jug.url.controller;

import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.LogoutResponse;
import com.jug.url.dto.response.ProfileResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseWrapper<ProfileResponse>userProfile() {
        return userService.userProfile();
    }

    @PostMapping("/signup")
    public ResponseWrapper<AuthResponse> addNewUser(@RequestBody @Valid CreateUserRequest payload) {
        return userService.signup(payload);
    }

    @PostMapping("/login")
    public ResponseWrapper<AuthResponse> authenticateAndGetToken(@RequestBody @Valid LoginRequest payload) {
        return userService.login(payload);
    }

    @PutMapping("/logout")
    public ResponseWrapper<LogoutResponse> logout(){
        return userService.logout();
    }
}