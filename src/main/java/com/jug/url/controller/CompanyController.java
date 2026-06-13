package com.jug.url.controller;

import com.jug.url.dto.request.CreateCompanyRequest;
import com.jug.url.dto.response.CompanyProfile;
import com.jug.url.dto.response.CreateCompanyResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.service.CompanyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/create-company")
    public  ResponseWrapper<CreateCompanyResponse> createCompany(@RequestBody @Valid CreateCompanyRequest payload){
        return companyService.createCompany(payload);
    }

    @GetMapping("/company-profile")
    public ResponseWrapper<CompanyProfile> getCompanyProfile(@RequestParam(name = "adminId",required = false)UUID adminId){
        return companyService.fetchCompanyProfileByAdminId(adminId);
    }

    @GetMapping("/company-profile/{id}")
    public ResponseWrapper<CompanyProfile> getCompanyProfileById(@PathVariable("id") UUID id){
        return companyService.fetchCompanyProfile(id);
    }
}