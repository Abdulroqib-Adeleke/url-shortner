package com.jug.url.controller;

import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.LogoutResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/sysadmin/signup")
    public ResponseWrapper<AuthResponse> signupSystemAdmin(@RequestBody @Valid CreateUserRequest payload) {
        return userService.signupSystemAmin(payload);
    }

    @PostMapping("/agent/signup")
    public ResponseWrapper<AuthResponse> signupAgent(@RequestBody @Valid CreateUserRequest payload) {
        return userService.signupAgent(payload);
    }

    @PostMapping("/login")
    public  ResponseWrapper<AuthResponse> login(@RequestBody @Valid LoginRequest payload) {
        return  userService.login(payload);
    }

    @PutMapping("/logout")
    public  ResponseWrapper<LogoutResponse> logout() {
        return  userService.logOut();
    }
}
