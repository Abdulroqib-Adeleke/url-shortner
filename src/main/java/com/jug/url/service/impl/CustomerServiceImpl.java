package com.jug.url.service.impl;

import com.jug.url.dto.request.CreateCustomerRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.CompanyProfile;
import com.jug.url.dto.response.CustomerProfile;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.exceptions.ResourceNotFoundException;
import com.jug.url.model.CustomerModel;
import com.jug.url.repository.CustomerRepository;
import com.jug.url.service.CompanyService;
import com.jug.url.service.CustomerService;
import com.jug.url.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private  final CustomerRepository customerRepository;
    private final UserService userService;
    private  final CompanyService companyService;

    @Override
    //TODO: implement this
    public ResponseWrapper<CustomerProfile> getCustomerProfileByUserId(UUID userId) {
        return null;
    }

    @Override
    @Transactional
    public ResponseWrapper<AuthResponse> createCustomer(CreateCustomerRequest payload) {
        ResponseWrapper<CompanyProfile> companyProfileResponse = companyService.fetchCompanyById(payload.getCompanyId());
        CompanyProfile companyProfile = companyProfileResponse.getData();
        if (companyProfile == null) throw new ResourceNotFoundException("Error occurred: company not found!");

        ResponseWrapper<AuthResponse> authResponseResponse = userService.signupCustomer(payload);
        AuthResponse authResponse = authResponseResponse.getData();
        CustomerModel customer = CustomerModel.builder()
                .companyId(payload.getCompanyId())
                .userId(authResponse.getUserId())
                .name(payload.getName())
                .email(payload.getEmail())
                .build();

        customerRepository.save(customer);

        return authResponseResponse;
    }
}