package com.jug.url.module.urlmodule.service.impl;

import com.jug.url.dto.proxy.CustomerProxy;
import com.jug.url.dto.request.UrlDto;
import com.jug.url.dto.response.CompanyProfile;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.dto.response.ServiceOfferingResponse;
import com.jug.url.dto.response.UrlResponse;
import com.jug.url.enums.ServiceOffering;
import com.jug.url.exceptions.AccessDeniedException;
import com.jug.url.exceptions.ResourceNotFoundException;
import com.jug.url.model.UrlShortenerModel;
import com.jug.url.module.urlmodule.repository.UrlShortenerRepository;
import com.jug.url.module.urlmodule.service.UrlService;
import com.jug.url.service.CompanyService;
import com.jug.url.utils.SecurityUtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlShortenerRepository urlShortenerRepository;
    private final CompanyService companyService;
    private final SecurityUtilsService securityUtilsService;

    @Override
    public ResponseWrapper<UrlResponse> generateShortUrl(UrlDto payload) {
        CustomerProxy loggedInCustomer  = securityUtilsService.getAuthenticatedCustomer();
        CompanyProfile companyProfile = validateCompanyModules(loggedInCustomer.getCompanyId());

        String shortCode;
        do {
            shortCode = generateRandomCode(8);
        }while (urlShortenerRepository.existsByShortCode(shortCode));

        UrlShortenerModel shortenerModel = UrlShortenerModel.builder()
                .longUrl(payload.getUrl())
                .shortCode(shortCode)
                .companyId(loggedInCustomer.getCompanyId())
                .customerId(loggedInCustomer.getId())
                .build();
        urlShortenerRepository.save(shortenerModel);
        String companyBaseUrl  = companyProfile.getBaseUrl();
        String updatedUrl = companyBaseUrl + shortCode;
        UrlResponse response = new UrlResponse(updatedUrl);
        return ResponseWrapper.<UrlResponse>builder()
                .data(response)
                .message("url shortened successfully")
                .statusCode(HttpStatus.CREATED)
                .build();
    }

    private CompanyProfile validateCompanyModules(UUID companyId) {
        ResponseWrapper<CompanyProfile> companyProfileResponseWrapper = companyService.fetchCompanyProfile(companyId);
        CompanyProfile companyProfile = companyProfileResponseWrapper.getData();
        if (companyProfile == null) throw  new ResourceNotFoundException("Company profile not found!");

        List<ServiceOfferingResponse> modules = companyProfile.getModules();
        Set<ServiceOffering> serviceOfferings = new HashSet<>();
        for (ServiceOfferingResponse response : modules){
            serviceOfferings.add(response.getService());
        }

        if (!serviceOfferings.contains(ServiceOffering.URL_SHORTNER_SERVICE)) throw new AccessDeniedException("Unauthorized activity");
        return companyProfile;
    }

    @Override
    public ResponseWrapper<UrlResponse> redirectToLongUrl(String shortCode) {
        Optional<UrlShortenerModel> urlShortenerModelOptional = urlShortenerRepository.findByShortCode(shortCode);
        if (urlShortenerModelOptional.isEmpty()) throw new ResourceNotFoundException("Url resource not found!");
        UrlShortenerModel shortenerModel = urlShortenerModelOptional.get();
        validateCompanyModules(shortenerModel.getCompanyId());

        UrlResponse response = new UrlResponse(shortenerModel.getLongUrl());
        return ResponseWrapper.<UrlResponse>builder()
                .data(response)
                .message("url shortened successfully")
                .statusCode(HttpStatus.CREATED)
                .build();
    }




    private static  String generateRandomCode(int length){
        String base62String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(); //""
        for (int i = 0; i < length; i++){
            sb.append(
                    base62String.charAt(
                            random.nextInt(
                                    base62String.length()
                            )
                    )
            );
        }
        return  sb.toString();
    }
}
