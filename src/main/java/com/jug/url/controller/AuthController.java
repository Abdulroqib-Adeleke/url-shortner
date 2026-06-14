package com.jug.url.controller;

import com.jug.url.dto.request.CreateCustomerRequest;
import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.request.RefreshTokenRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.LoginResponse;
import com.jug.url.dto.response.LogoutResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.service.CustomerService;
import com.jug.url.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
        return "Welcome to A B2B2C SaaS Platform";
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
    public  ResponseWrapper<LoginResponse> login(@RequestBody @Valid LoginRequest payload) {
        return  userService.login(payload);
    }

    @Operation(security =@SecurityRequirement(name = "X-COMPANY_ID"))
    @PostMapping("/customer/login")
    public  ResponseWrapper<LoginResponse> customerLogin(@RequestBody @Valid LoginRequest payload,
                                                        @RequestHeader("X-COMPANY_ID") UUID companyId) {
        return  userService.customerLogin(payload, companyId);
    }

    @PostMapping("/refresh-token")
    public ResponseWrapper<LoginResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest payload){

        return userService.refreshToken(payload);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/logout")
    public  ResponseWrapper<LogoutResponse> logout() {
        return  userService.logOut();
    }
}
