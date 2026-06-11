package com.jug.url.service.impl;

import com.jug.url.dto.proxy.CustomerProxy;
import com.jug.url.dto.proxy.UserProxy;
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
import com.jug.url.utils.SecurityUtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private  final CustomerRepository customerRepository;
    private final UserService userService;
    private  final CompanyService companyService;
    private final SecurityUtilsService securityUtilsService;

    @Override
    public ResponseWrapper<CustomerProfile> getCustomerProfileByUserId(UUID userId) {

        Optional<CustomerProxy> customerProxyOptional = customerRepository.findCustomerByUserId(userId);
        if(customerProxyOptional.isEmpty()) throw new ResourceNotFoundException("Customer not found");
        CustomerProxy customerProxy = customerProxyOptional.get();

        CustomerProfile customerProfile = new CustomerProfile(customerProxy.getId(),
                customerProxy.getName(),
                customerProxy.getEmail(),
                customerProxy.getUserId(),
                customerProxy.getCompanyId(),
                customerProxy.getCreatedDate(),
                customerProxy.getUpdatedDate(),
                customerProxy.getCompanyName(),
                customerProxy.getAdminId());
        return ResponseWrapper.<CustomerProfile>builder()
                .data(customerProfile)
                .statusCode(HttpStatus.OK)
                .message("Customer profile fetched successfully")
                .build();
    }

    @Override
    public ResponseWrapper<CustomerProfile> fetchCustomerProfile() {
        UserProxy loggedInUser = securityUtilsService.getSecurityPrincipal();
        securityUtilsService.validateCustomerInRole();
        return getCustomerProfileByUserId(loggedInUser.getId());
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