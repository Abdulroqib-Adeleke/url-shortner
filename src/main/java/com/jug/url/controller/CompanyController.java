package com.jug.url.controller;

import com.jug.url.dto.request.CreateCompanyRequest;
import com.jug.url.dto.response.CompanyProfile;
import com.jug.url.dto.response.CreateCompanyResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/agent")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/create")
    public ResponseWrapper<CreateCompanyResponse> createCompany(@RequestBody @Valid CreateCompanyRequest payload){
        return companyService.createCompany(payload);
    }

    @GetMapping("/profile")
    public ResponseWrapper<CompanyProfile> getAgentProfile(@RequestBody @Valid UUID id){
        return companyService.fetchCompanyProfile(id);
    }
}
