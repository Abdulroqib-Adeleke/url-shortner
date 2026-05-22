package com.jug.url.service.impl;

import com.jug.url.dto.proxy.UserProxy;
import com.jug.url.dto.request.CreateCompanyRequest;
import com.jug.url.dto.response.CreateCompanyResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.exceptions.AccessDeniedException;
import com.jug.url.exceptions.BadRequestException;
import com.jug.url.model.Company;
import com.jug.url.repository.CompanyRepository;
import com.jug.url.service.CompanyService;
import com.jug.url.utils.SecurityUtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final SecurityUtilsService securityUtilsService;

    @Override
    public ResponseWrapper<CreateCompanyResponse> createCompany(CreateCompanyRequest payload) {
        Optional<UserProxy> loggedInUser = securityUtilsService.getPrincipal();
        if (loggedInUser.isEmpty()) throw new AccessDeniedException("error occurred: access denied");
        if (companyRepository.existsByAdminId(loggedInUser.get().getId())) throw new BadRequestException("multiple companies not allowed");
        Optional<Company> companyOptional = companyRepository.findByName(payload.getCompanyName());

        if (companyOptional.isPresent()){
            String message = String.format("%s is already taken", payload.getCompanyName());
            throw new BadRequestException(message);
        }

        Company company = Company.builder()
                .name(payload.getCompanyName())
                .supportEmail(payload.getEmail())
                .adminId(loggedInUser.get().getId())
                .build();
        Company savedCompany = companyRepository.save(company);
        CreateCompanyResponse response = new CreateCompanyResponse(String.valueOf(savedCompany.getId()));

        return ResponseWrapper.<CreateCompanyResponse>builder()
                .data(response)
                .statusCode(HttpStatus.CREATED)
                .message("Company successfully created")
                .build();
    }
}
