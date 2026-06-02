package com.jug.url.service.impl;

import com.jug.url.dto.proxy.UserProxy;
import com.jug.url.dto.request.CreateCompanyRequest;
import com.jug.url.dto.response.CompanyProfile;
import com.jug.url.dto.response.CreateCompanyResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.enums.UserType;
import com.jug.url.exceptions.AccessDeniedException;
import com.jug.url.exceptions.BadRequestException;
import com.jug.url.model.Company;
import com.jug.url.repository.CompanyRepository;
import com.jug.url.service.CompanyService;
import com.jug.url.utils.SecurityUtilsService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final SecurityUtilsService securityUtilsService;

    @Override
    public ResponseWrapper<CreateCompanyResponse> createCompany(CreateCompanyRequest payload) {
        Optional<UserProxy> loggedInUser = securityUtilsService.getPrincipal();
        if (loggedInUser.isEmpty()) throw new AccessDeniedException("error occurred: access denied");

        if (companyRepository.existsByAdminId(loggedInUser.get().getId())) throw  new BadRequestException("multiple companies not allowed!");

        Optional<Company> companyOptional = companyRepository.findByName(payload.getCompanyName());
        if (companyOptional.isPresent()){
            String message = String.format("%s is already taken!",payload.getCompanyName());
            throw new BadRequestException(message);
        }
        Company company = Company.builder()
                .name(payload.getCompanyName())
                .supportEmail(payload.getSupportEmail())
                .adminId(loggedInUser.get().getId())
                .build();

        Company savedCompany = companyRepository.save(company);
        CreateCompanyResponse response = new CreateCompanyResponse(String.valueOf(savedCompany.getId()));
        return ResponseWrapper.<CreateCompanyResponse>builder()
                .data(response)
                .statusCode(HttpStatus.CREATED)
                .message("Company successfully created!")
                .build();
    }

    @Override
    public ResponseWrapper<CompanyProfile> fetchCompanyProfile(UUID adminId) {
        Optional<UserProxy> loggedInUser = securityUtilsService.getPrincipal();
        if (loggedInUser.isEmpty()) throw new AccessDeniedException("error occurred: access denied");
        UserProxy userProxy = loggedInUser.get();

        UUID callerAdminId = getCallerAdminId(userProxy.getUserType(), adminId, userProxy);

        Optional<CompanyProfile> companyProfileOptional = companyRepository.fetchCompanyProfile(callerAdminId);

        CompanyProfile profile = companyProfileOptional.orElse(null);

        return ResponseWrapper.<CompanyProfile>builder()
                .data(profile)
                .statusCode(HttpStatus.OK)
                .message(profile==null ? "no company created yet" : "company profile fetch successfully")
                .build();
    }

    private UUID getCallerAdminId(UserType userType, @Nullable UUID adminId, UserProxy userProxy){
        if (userType.equals(UserType.ADMIN)) return userProxy.getId();
        if (userType.equals(UserType.SYSTEM_ADMIN)) return adminId;

        return userProxy.getId();
    }
}
