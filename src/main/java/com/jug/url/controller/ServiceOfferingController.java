package com.jug.url.controller;

import com.jug.url.dto.request.CreateAgentProductOfferingRequest;
import com.jug.url.dto.request.CreatePrincipalProductOfferingRequest;
import com.jug.url.dto.response.CreateProductResponse;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.dto.response.ServiceOfferingResponse;
import com.jug.url.service.ProductOfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/service")
@RequiredArgsConstructor
@Slf4j
public class ServiceOfferingController {

    private final ProductOfferingService productOfferingService;

    @PostMapping("/principal/create-service")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseWrapper<CreateProductResponse> createPrincipalProduct(@RequestBody @Valid CreatePrincipalProductOfferingRequest payload){
        return  productOfferingService.createPrincipalService(payload);
    }

    @PutMapping("/principal/disable-service/{serviceId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseWrapper<CreateProductResponse> disablePrincipalProduct(@PathVariable(name = "serviceId") UUID serviceId){
        return  productOfferingService.disablePrincipalService(serviceId);
    }

    @GetMapping("/fetch-system-service")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') and hasRole('ADMIN')")
    public ResponseWrapper<List<ServiceOfferingResponse>> fetchPrincipalProduct(){
        return  productOfferingService.fetchImplementedService();
    }

    @PostMapping("/agent/create-service")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') and hasRole('ADMIN')")
    public ResponseWrapper<CreateProductResponse> createAgentProduct(@RequestBody @Valid CreateAgentProductOfferingRequest payload){
        return  productOfferingService.createAgentService(payload);
    }
    @PostMapping("/agent/list-service")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') and hasRole('ADMIN')")
    public ResponseWrapper<List<ServiceOfferingResponse>> fetchAgentProduct(@RequestParam(name = "adminId",required = false)UUID adminId){
        return  productOfferingService.fetchAgentService(adminId);
    }
    @PutMapping("/agent/disable-service/{serviceId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') and hasRole('ADMIN')")
    public ResponseWrapper<CreateProductResponse> disableAgentProduct(@PathVariable(name = "serviceId") UUID serviceId){
        return  productOfferingService.disablePrincipalService(serviceId);
    }

}

