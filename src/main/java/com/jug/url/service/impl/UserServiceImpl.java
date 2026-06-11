package com.jug.url.service.impl;

import com.jug.url.auth.JwtService;
import com.jug.url.dto.helper.SavedUserResponse;
import com.jug.url.dto.proxy.CustomerProxy;
import com.jug.url.dto.proxy.UserProxy;
import com.jug.url.dto.request.CreateUserRequest;
import com.jug.url.dto.request.LoginRequest;
import com.jug.url.dto.response.*;
import com.jug.url.enums.Roles;
import com.jug.url.enums.UserType;
import com.jug.url.exceptions.AccessDeniedException;
import com.jug.url.exceptions.BadRequestException;
import com.jug.url.exceptions.ResourceNotFoundException;
import com.jug.url.model.UserModel;
import com.jug.url.repository.CustomerRepository;
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
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private  final UserModelRepository userModelRepository;
    private final UserLoginSessionService userLoginSessionService;
    private  final PasswordEncoder passwordEncoder;
    private final  AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private  final SecurityUtilsService securityUtilsService;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public ResponseWrapper<AuthResponse> signupAgent(CreateUserRequest payload) {
        log.info("AUTH LOG: signup payload: {}",payload);

        Optional<UserModel> userModel = userModelRepository.findByEmail(payload.getEmail());
        if (userModel.isPresent()) throw new BadRequestException("Error occupied,please use another email!");
        Set<Roles> agentRoles = Set.of(Roles.ADMIN);


        SavedUserResponse savedUserResponse = saveUserDetailsAndSession(payload.getName(),payload.getEmail(),payload.getPassword(),agentRoles);

        return buildAuthResponse(savedUserResponse.getUserId(),savedUserResponse.getToken(),
                "Signup successful",
                HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
    }

    @Override
    public ResponseWrapper<AuthResponse> signupSystemAmin(CreateUserRequest payload) {
        log.info("AUTH LOG: SYSADMIN signup payload: {}",payload);

        Optional<UserModel> userModel = userModelRepository.findByEmail(payload.getEmail());
        if (userModel.isPresent()) throw new BadRequestException("Error occupied,please use another email!");
        Set<Roles> systemAdminRole = Set.of(Roles.SYSTEM_ADMIN);

        SavedUserResponse savedUserResponse = saveUserDetailsAndSession(payload.getName(),payload.getEmail(),payload.getPassword(),systemAdminRole);

        return buildAuthResponse(savedUserResponse.getUserId(),savedUserResponse.getToken(),
                "Signup successful",
                HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
    }

    @Override
    public ResponseWrapper<AuthResponse> signupCustomer(CreateUserRequest payload) {
        Set<Roles> customerRoles = Set.of(Roles.USER);
        SavedUserResponse savedUserResponse = saveUserDetailsAndSession(payload.getName(),payload.getEmail(),payload.getPassword(),customerRoles);

        return buildAuthResponse(savedUserResponse.getUserId(),savedUserResponse.getToken(),
                "Signup successful",
                HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
    }

    @Override
    public ResponseWrapper<AuthResponse> login(LoginRequest payload) {
        Optional<UserModel> userModelOptional = userModelRepository.findByEmail(payload.getEmail());
        if (userModelOptional.isEmpty()) throw new ResourceNotFoundException("User not found!");
        UserModel user = userModelOptional.get();
        log.info("User details {}",user);
        return getAuthResponseResponseWrapper(payload, user);
    }

    @Override
    public ResponseWrapper<AuthResponse> customerLogin(LoginRequest payload, UUID companyId) {
        try{
            Optional<CustomerProxy>customerProxyOptional = customerRepository.findCustomerByEmailAndCompanyId(payload.getEmail(), companyId);
            if(customerProxyOptional.isEmpty()) throw new AccessDeniedException("Error: occurred: invalid credentials");
            CustomerProxy customerProxy = customerProxyOptional.get();

            Optional<UserModel> userModelOptional = userModelRepository.findById(customerProxy.getUserId());

            if(userModelOptional.isEmpty()) throw new AccessDeniedException("Invalid authentication credentials");

            return getAuthResponseResponseWrapper(payload, userModelOptional.get());
        }catch (Exception ex){
            log.error("Error occurred: " , ex);
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    public ResponseWrapper<LogoutResponse> logOut() {
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

    private SavedUserResponse saveUserDetailsAndSession(String name, String email, String password, Set<Roles> agentRoles){
        UserModel user = UserModel.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(agentRoles)
                .userType(UserType.SYSTEM_ADMIN)
                .build();
        UserModel savedUser = userModelRepository.save(user);
        String sessionId = LocalDateTime.now().toString();
        userLoginSessionService.createLoginSession(sessionId,user.getId());
        String token  = jwtService.generateToken(email, agentRoles,sessionId,savedUser.getId());
        return new SavedUserResponse(savedUser.getId(),token);
    }

    private ResponseWrapper<AuthResponse> getAuthResponseResponseWrapper(LoginRequest payload, UserModel user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getId().toString(), payload.getPassword())
            );

            String sessionId = LocalDateTime.now().toString();
            String token = jwtService.generateToken(payload.getEmail(), user.getRoles(),sessionId, user.getId());


            userLoginSessionService.createLoginSession(sessionId, user.getId());

            return buildAuthResponse(user.getId(), token, "Login Successful", HttpStatusCode.valueOf(HttpStatus.OK.value()));
        }catch (BadCredentialsException ex){
            log.error("Error occurred: ",ex);
            throw new AccessDeniedException("Invalid authentication credentials");
        }
    }
}
