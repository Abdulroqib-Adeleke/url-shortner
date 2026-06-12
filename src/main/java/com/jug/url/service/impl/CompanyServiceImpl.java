package com.jug.url.service.impl;

import com.jug.url.dto.proxy.AgentServiceOfferingProxy;
import com.jug.url.dto.proxy.UserProxy;
import com.jug.url.dto.request.CreateCompanyRequest;
import com.jug.url.dto.response.CompanyProfile;
import com.jug.url.dto.response.CreateCompanyResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.dto.response.ServiceOfferingResponse;
import com.jug.url.enums.UserType;
import com.jug.url.exceptions.AccessDeniedException;
import com.jug.url.exceptions.BadRequestException;
import com.jug.url.exceptions.ResourceNotFoundException;
import com.jug.url.model.Company;
import com.jug.url.repository.AgentServiceOfferingRepository;
import com.jug.url.repository.CompanyRepository;
import com.jug.url.service.CompanyService;
import com.jug.url.utils.SecurityUtilsService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private  final CompanyRepository companyRepository;
    private  final SecurityUtilsService securityUtilsService;
    private final AgentServiceOfferingRepository agentServiceOfferingRepository;

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
    public ResponseWrapper<CompanyProfile> fetchCompanyProfileByAdminId(UUID adminId) {

        Optional<UserProxy> loggedInUser = securityUtilsService.getPrincipal();
        if (loggedInUser.isEmpty()) throw new AccessDeniedException("error occurred: access denied");
        UserProxy user = loggedInUser.get();
        UUID callerAdminId = getCallerAdminId(user.getUserType(),adminId,user);
        Optional<CompanyProfile> companyProfileOptional = companyRepository.fetchCompanyProfile(callerAdminId);

        CompanyProfile profile = companyProfileOptional.orElse(null);

        return ResponseWrapper.<CompanyProfile>builder()
                .data(profile)
                .statusCode(HttpStatus.OK)
                .message(profile == null? "no company created yet" : "Company profile fetched successfully")
                .build();
    }

    @Override
    public ResponseWrapper<CompanyProfile> fetchCompanyProfile(UUID companyId) {
        return fetchCompanyById(companyId);
    }

    @Override
    public ResponseWrapper<CompanyProfile> fetchCompanyById(UUID companyId) {
        Optional<CompanyProfile> companyProfileOptional = companyRepository.fetchCompanyProfileById(companyId);
        if (companyProfileOptional.isEmpty()) throw new ResourceNotFoundException("Company not found!");
        CompanyProfile companyProfile = companyProfileOptional.get();
        List<AgentServiceOfferingProxy> serviceOfferingList = agentServiceOfferingRepository.getAgentActiveServicesByCompanyId(companyProfile.getId());
        List<ServiceOfferingResponse> services = serviceOfferingList.stream()
                .map(item ->new ServiceOfferingResponse(item.getService(),item.getService().getDescription(),item.getId()))
                .toList();

        companyProfile.setModules(services);

        return ResponseWrapper.<CompanyProfile>builder()
                .data(companyProfile)
                .statusCode(HttpStatus.OK)
                .message("Company profile fetched successfully")
                .build();
    }

    private UUID getCallerAdminId(UserType userType, @Nullable UUID adminId,UserProxy user){
        if (userType.equals(UserType.ADMIN)) return  user.getId();
        if (userType.equals(UserType.SYSTEM_ADMIN) && adminId != null) return  adminId;
        return  user.getId();
    }
}
