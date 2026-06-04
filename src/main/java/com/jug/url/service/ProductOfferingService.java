package com.jug.url.service;

import com.jug.url.dto.request.CreateAgentProductOfferingRequest;
import com.jug.url.dto.request.CreatePrincipalProductOfferingRequest;
import com.jug.url.dto.response.CreateProductResponse;
import com.jug.url.dto.response.EnableServiceRequests;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.dto.response.ServiceOfferingResponse;

import java.util.List;
import java.util.UUID;

public interface ProductOfferingService {

    ResponseWrapper<CreateProductResponse> createPrincipalService(CreatePrincipalProductOfferingRequest payload);
    ResponseWrapper<CreateProductResponse> createAgentService(CreateAgentProductOfferingRequest payload);
    ResponseWrapper<CreateProductResponse> disableAgentService(UUID serviceId);
    ResponseWrapper<CreateProductResponse> disablePrincipalService(UUID serviceId);
    ResponseWrapper<CreateProductResponse> enablePrincipalService(UUID serviceId);
    ResponseWrapper<CreateProductResponse> enableAgentService(UUID serviceId);
    ResponseWrapper<CreateProductResponse> createServiceEnableRequest(UUID serviceId);
    ResponseWrapper<List<ServiceOfferingResponse>> fetchImplementedService();
    ResponseWrapper<List<ServiceOfferingResponse>> fetchAgentService(UUID adminId);
    ResponseWrapper<List<EnableServiceRequests>> fetchAgentEnableServiceRequests();

}
