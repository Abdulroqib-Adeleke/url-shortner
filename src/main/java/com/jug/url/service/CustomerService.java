package com.jug.url.service;

import com.jug.url.dto.request.CreateCustomerRequest;
import com.jug.url.dto.response.AuthResponse;
import com.jug.url.dto.response.CustomerProfile;
import com.jug.url.dto.response.ResponseWrapper;

import java.util.UUID;

public interface CustomerService {

    ResponseWrapper<CustomerProfile> getCustomerProfileByUserId(UUID userId);
    ResponseWrapper<CustomerProfile> fetchCustomerProfile();
    ResponseWrapper<AuthResponse> createCustomer(CreateCustomerRequest payload);

}
