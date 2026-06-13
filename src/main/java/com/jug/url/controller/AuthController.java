package com.jug.url.controller;

import com.jug.url.dto.request.CreateCustomerRequest;
import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.LogoutResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.service.CustomerService;
import com.jug.url.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final CustomerService customerService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/agent/signup")
    public ResponseWrapper<AuthResponse> signupAgent(@RequestBody @Valid CreateUserRequest payload) {
        return userService.signupAgent(payload);
    }

    @PostMapping("/customer/signup")
    public ResponseWrapper<AuthResponse> signupCustomer(@RequestBody @Valid CreateCustomerRequest payload) {
        return customerService.createCustomer(payload);
    }

    @PostMapping("/login")
    public  ResponseWrapper<AuthResponse> login(@RequestBody @Valid LoginRequest payload) {
        return  userService.login(payload);
    }

    @PostMapping("/customer/login")
    public  ResponseWrapper<AuthResponse> customerLogin(@RequestBody @Valid LoginRequest payload,
                                                        @RequestHeader("X-COMPANY_ID") UUID companyId) {
        return  userService.customerLogin(payload, companyId);
    }

    @PutMapping("/logout")
    public  ResponseWrapper<LogoutResponse> logout() {
        return  userService.logOut();
    }
}
