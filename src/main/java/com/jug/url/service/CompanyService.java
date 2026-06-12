package com.jug.url.service;

import com.jug.url.dto.request.CreateCompanyRequest;
import com.jug.url.dto.response.CompanyProfile;
import com.jug.url.dto.response.CreateCompanyResponse;
import com.jug.url.dto.response.ResponseWrapper;

import java.util.UUID;

public interface CompanyService {

    ResponseWrapper<CreateCompanyResponse> createCompany(CreateCompanyRequest payload);
    ResponseWrapper<CompanyProfile> fetchCompanyProfileByAdminId(UUID adminId);
    ResponseWrapper<CompanyProfile> fetchCompanyProfile(UUID companyId);
    ResponseWrapper<CompanyProfile> fetchCompanyById(UUID companyId);
}
