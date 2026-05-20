package com.jug.url.service.impl;

import com.jug.url.auth.JwtService;
import com.jug.url.dto.proxy.UserProxy;
import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.LogoutResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.exceptions.AccessDeniedException;
import com.jug.url.exceptions.BadRequestException;
import com.jug.url.exceptions.ResourceNotFoundException;
import com.jug.url.model.UserModel;
import com.jug.url.repository.UserModelRepository;
import com.jug.url.service.UserLoginSessionService;
import com.jug.url.service.UserService;
import com.jug.url.utils.SecurityUtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserModelRepository userModelRepository;
    private final UserLoginSessionService userLoginSessionService;
    private  final PasswordEncoder passwordEncoder;
    private final  AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SecurityUtilsService securityUtilsService;

    @Override
    @Transactional
    public ResponseWrapper<AuthResponse> signup(CreateUserRequest payload) {

        Optional<UserModel> userModel = userModelRepository.findByEmail(payload.getEmail());
        if(userModel.isPresent()) throw new BadRequestException("Error occurred use another email");

        UserModel user = UserModel.builder()
                .name(payload.getName())
                .email(payload.getEmail())
                .password(passwordEncoder.encode(payload.getPassword()))
                .roles(payload.getRoles())
                .build();
        UserModel savedUser = userModelRepository.save(user);
        String sessionId = LocalDateTime.now().toString();
        String token = jwtService.generateToken(payload.getEmail(), payload.getRoles(), sessionId, savedUser.getId());
        return buildAuthResponse(savedUser.getId(),token,
                "Signup successful",
                HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
    }

    @Override
    public ResponseWrapper<AuthResponse> login(LoginRequest payload) {
        Optional<UserModel> userModelOptional = userModelRepository.findByEmail(payload.getEmail());
        if (userModelOptional.isEmpty()) throw new ResourceNotFoundException("User not found!");
        UserModel user = userModelOptional.get();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword())
            );

            String sessionId = LocalDateTime.now().toString();
            String token = jwtService.generateToken(payload.getEmail(), user.getRoles(),sessionId,user.getId());


            userLoginSessionService.createLoginSession(sessionId,user.getId());

            return  buildAuthResponse(user.getId(),token,"Login Successful",HttpStatusCode.valueOf(HttpStatus.OK.value()));
        }catch (BadCredentialsException ex){
            log.error("Error occurred: ",ex);
            throw new AccessDeniedException("Invalid authentication credentials");
        }
    }

    @Override
    public ResponseWrapper<LogoutResponse> logout() {
        Optional<UserProxy> userProxyOptional = securityUtilsService.getPrincipal();
        if (userProxyOptional.isEmpty()) throw new ResourceNotFoundException("User not found!");
        userLoginSessionService.invalidateLoginSession(userProxyOptional.get().getId());
        LogoutResponse response = new LogoutResponse(UUID.randomUUID());
        return ResponseWrapper.<LogoutResponse>builder()
                .message("Logout successful")
                .statusCode(HttpStatus.OK)
                .data(response)
                .build();
    }


    private ResponseWrapper<AuthResponse> buildAuthResponse(UUID id, String token, String message, HttpStatusCode statusCode){
        AuthResponse response = new AuthResponse(id,token);
        return ResponseWrapper.<AuthResponse>builder()
                .data(response)
                .message(message)
                .statusCode(statusCode)
                .build();
    }
}