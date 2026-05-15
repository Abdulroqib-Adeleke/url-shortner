package com.jug.url.controller;

import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/signup")
    public ResponseWrapper<AuthResponse> addNewUser(@RequestBody CreateUserRequest payload) {
        return userService.signup(payload);
    }

    @PostMapping("/login")
    public ResponseWrapper<AuthResponse> authenticateAndGetToken(@RequestBody LoginRequest payload) {
        return userService.login(payload);
    }
}