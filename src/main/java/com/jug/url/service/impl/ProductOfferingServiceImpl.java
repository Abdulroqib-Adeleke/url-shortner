package com.jug.url.service.impl;

import com.jug.url.dto.proxy.AgentServiceOfferingProxy;
import com.jug.url.dto.proxy.EnableServiceRequestProxy;
import com.jug.url.dto.proxy.PrincipalServiceProxy;
import com.jug.url.dto.proxy.UserProxy;
import com.jug.url.dto.request.CreateAgentProductOfferingRequest;
import com.jug.url.dto.request.CreatePrincipalProductOfferingRequest;
import com.jug.url.dto.response.*;
import com.jug.url.enums.ServiceOffering;
import com.jug.url.enums.ServiceRequestStatus;
import com.jug.url.enums.UserType;
import com.jug.url.exceptions.AccessDeniedException;
import com.jug.url.exceptions.ResourceNotFoundException;
import com.jug.url.model.AgentServiceOffering;
import com.jug.url.model.EnableServiceRequestModel;
import com.jug.url.model.PrincipalServiceOffering;
import com.jug.url.repository.AgentServiceOfferingRepository;
import com.jug.url.repository.EnableServiceRequestRepository;
import com.jug.url.repository.PrincipalServiceOfferingRepository;
import com.jug.url.service.CompanyService;
import com.jug.url.service.CustomerService;
import com.jug.url.service.ProductOfferingService;
import com.jug.url.utils.SecurityUtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductOfferingServiceImpl implements ProductOfferingService {
    private final PrincipalServiceOfferingRepository principalServiceOfferingRepository;
    private  final AgentServiceOfferingRepository agentServiceOfferingRepository;
    private  final EnableServiceRequestRepository enableServiceRequestRepository;
    private  final SecurityUtilsService securityUtilsService;
    private  final CompanyService companyService;
    private  final CustomerService customerService;


    @Override
    @Transactional
    public ResponseWrapper<CreateProductResponse> createPrincipalService(CreatePrincipalProductOfferingRequest payload) {

        UserProxy loggedInUser = getSecurityPrincipal();
        if (!loggedInUser.getUserType().equals(UserType.SYSTEM_ADMIN)) throw new AccessDeniedException("Unauthorized operation!");
        PrincipalServiceOffering serviceOffering = PrincipalServiceOffering.builder()
                .service(payload.getServiceOffering())
                .active(Boolean.TRUE)
                .build();

        PrincipalServiceOffering savedService =  principalServiceOfferingRepository.save(serviceOffering);

        return mapProductResponse(savedService.getId(), HttpStatus.CREATED,"Service created successfully");
    }

    @Override
    @Transactional
    public ResponseWrapper<CreateProductResponse> createAgentService(CreateAgentProductOfferingRequest payload) {
        UserProxy loggedInUser = getSecurityPrincipal();
        if (!loggedInUser.getUserType().equals(UserType.ADMIN) &&
                !loggedInUser.getUserType().equals(UserType.SYSTEM_ADMIN)) throw new AccessDeniedException("Unauthorized operation");

        ResponseWrapper<CompanyProfile> companyProfileResponse = companyService.fetchCompanyById(payload.getCompanyId());
        CompanyProfile companyProfile = companyProfileResponse.getData();
        if (companyProfile == null) throw new ResourceNotFoundException("Selected company not found!");
        Optional<PrincipalServiceOffering> serviceOffering = principalServiceOfferingRepository.findById(payload.getServiceOfferingId());
        if (serviceOffering.isEmpty()) throw new ResourceNotFoundException("Selected service not found!");
        AgentServiceOffering agentServiceOffering = AgentServiceOffering.builder()
                .principalServiceId(payload.getServiceOfferingId())
                .companyId(payload.getCompanyId())
                .active(Boolean.TRUE)
                .build();
        AgentServiceOffering savedOffering = agentServiceOfferingRepository.save(agentServiceOffering);

        return mapProductResponse(savedOffering.getId(),HttpStatus.CREATED,"Service created successfully");
    }



    @Override
    @Transactional
    public ResponseWrapper<CreateProductResponse> disableAgentService(UUID serviceId) {
        UserProxy loggedInUser = getSecurityPrincipal();
        if (!loggedInUser.getUserType().equals(UserType.ADMIN) &&
                !loggedInUser.getUserType().equals(UserType.SYSTEM_ADMIN)) throw new AccessDeniedException("Unauthorized operation");

        Optional<AgentServiceOffering> agentServiceOfferingOptional = agentServiceOfferingRepository.findById(serviceId);
        if (agentServiceOfferingOptional.isEmpty()) throw new ResourceNotFoundException("Service not found!");
        AgentServiceOffering serviceOffering = agentServiceOfferingOptional.get();

        serviceOffering.setActive(Boolean.FALSE);
        agentServiceOfferingRepository.save(serviceOffering);

        return mapProductResponse(serviceId,HttpStatus.OK,"Service disabled successfully");
    }

    @Override
    @Transactional
    public ResponseWrapper<CreateProductResponse> disablePrincipalService(UUID serviceId) {
        UserProxy loggedInUser = getSecurityPrincipal();
        if (!loggedInUser.getUserType().equals(UserType.SYSTEM_ADMIN)) throw new AccessDeniedException("Unauthorized operation");
        Optional<PrincipalServiceOffering> serviceOfferingOptional = principalServiceOfferingRepository.findById(serviceId);

        if (serviceOfferingOptional.isEmpty()) throw new ResourceNotFoundException("Service not found");
        PrincipalServiceOffering serviceOffering = serviceOfferingOptional.get();
        serviceOffering.setActive(Boolean.FALSE);
        principalServiceOfferingRepository.save(serviceOffering);
        return mapProductResponse(serviceId,HttpStatus.OK,"Service disabled successfully");
    }

    @Override
    @Transactional
    public ResponseWrapper<CreateProductResponse> enablePrincipalService(UUID serviceId) {
        UserProxy loggedInUser = getSecurityPrincipal();
        if (!loggedInUser.getUserType().equals(UserType.SYSTEM_ADMIN)) throw new AccessDeniedException("Unauthorized operation");
        Optional<PrincipalServiceOffering> serviceOfferingOptional = principalServiceOfferingRepository.findById(serviceId);

        if (serviceOfferingOptional.isEmpty()) throw new ResourceNotFoundException("Service not found");
        PrincipalServiceOffering serviceOffering = serviceOfferingOptional.get();
        serviceOffering.setActive(Boolean.TRUE);
        principalServiceOfferingRepository.save(serviceOffering);
        return mapProductResponse(serviceId,HttpStatus.OK,"Service enabled successfully");
    }

    @Override
    @Transactional
    public ResponseWrapper<CreateProductResponse> enableAgentService(UUID serviceId) {
        UserProxy loggedInUser = getSecurityPrincipal();
        if (!loggedInUser.getUserType().equals(UserType.SYSTEM_ADMIN)) throw new AccessDeniedException("Unauthorized operation");

        Optional<AgentServiceOffering> agentServiceOfferingOptional = agentServiceOfferingRepository.findById(serviceId);
        if (agentServiceOfferingOptional.isEmpty()) throw new ResourceNotFoundException("Service not found!");
        AgentServiceOffering serviceOffering = agentServiceOfferingOptional.get();

        serviceOffering.setActive(Boolean.TRUE);
        agentServiceOfferingRepository.save(serviceOffering);

        return mapProductResponse(serviceId,HttpStatus.OK,"Service enabled successfully");
    }

    @Override
    @Transactional
    public ResponseWrapper<CreateProductResponse> createServiceEnableRequest(UUID serviceId) {
        UserProxy loggedInUser = getSecurityPrincipal();
        if (!loggedInUser.getUserType().equals(UserType.ADMIN)) throw new AccessDeniedException("Unauthorized operation");

        Optional<AgentServiceOffering> agentServiceOfferingOptional = agentServiceOfferingRepository.findById(serviceId);
        if (agentServiceOfferingOptional.isEmpty()) throw new ResourceNotFoundException("Service not found!");

        EnableServiceRequestModel serviceRequestModel = EnableServiceRequestModel.builder()
                .serviceId(serviceId)
                .adminId(loggedInUser.getId())
                .build();
        EnableServiceRequestModel savedService = enableServiceRequestRepository.save(serviceRequestModel);
        return mapProductResponse(savedService.getId(),HttpStatus.OK,"Service request sent successfully");
    }

    @Override
    public ResponseWrapper<List<ServiceOfferingResponse>> fetchImplementedService() {
        UserProxy loggedInUser = getSecurityPrincipal();
        if (loggedInUser.getUserType().equals(UserType.SYSTEM_ADMIN)){
            Set<ServiceOffering> serviceOfferings = ServiceOffering.listImplementedServices();
            List<ServiceOfferingResponse> services = serviceOfferings.stream().map(item -> new ServiceOfferingResponse(item,item.getDescription(),null)).toList();
            return ResponseWrapper.<List<ServiceOfferingResponse>>builder()
                    .data(services)
                    .message("Service fetched successfully")
                    .statusCode(HttpStatus.OK)
                    .build();
        }
        List<PrincipalServiceProxy> serviceProxies = principalServiceOfferingRepository.fetchImplementedProducts();
        List<ServiceOfferingResponse> services =  serviceProxies.stream().map(item ->new ServiceOfferingResponse(item.getService(),item.getService().getDescription(),item.getId())).toList();

        return ResponseWrapper.<List<ServiceOfferingResponse>>builder()
                .data(services)
                .message("Service fetched successfully")
                .statusCode(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseWrapper<List<ServiceOfferingResponse>>   fetchAgentService(final UUID adminId) {
        UserProxy loggedInUser = getSecurityPrincipal();

        List<AgentServiceOfferingProxy> agentServiceOfferingProxyList = new ArrayList<>();

        if (loggedInUser.getUserType().equals(UserType.SYSTEM_ADMIN)){
            agentServiceOfferingProxyList = agentServiceOfferingRepository.getAgentActiveServices(adminId);
        }else if (loggedInUser.getUserType().equals(UserType.ADMIN)){
            agentServiceOfferingProxyList = agentServiceOfferingRepository.getAgentActiveServices(loggedInUser.getId());
        }else {
            ResponseWrapper<CustomerProfile> customerProfileResponseWrapper = customerService.getCustomerProfileByUserId(loggedInUser.getId());
            if (customerProfileResponseWrapper.getData() == null) throw new AccessDeniedException("error occurred!");
            agentServiceOfferingProxyList = agentServiceOfferingRepository.getAgentActiveServices(customerProfileResponseWrapper.getData().adminId());

        }
        List<ServiceOfferingResponse> responseList = agentServiceOfferingProxyList.stream()
                .map(item-> new ServiceOfferingResponse(item.getService(),item.getService().getDescription(),item.getId()))
                .toList();
        return ResponseWrapper.<List<ServiceOfferingResponse>>builder()
                .data(responseList)
                .message("Service fetched successfully")
                .statusCode(HttpStatus.OK)
                .build();


    }

    @Override
    public ResponseWrapper<List<EnableServiceRequests>> fetchAgentEnableServiceRequests() {
        securityUtilsService.validateSystemAdminInRole();
        List<EnableServiceRequestProxy> enableServiceRequestProxyList = enableServiceRequestRepository.fetchPendingServiceRequest(ServiceRequestStatus.PENDING);
        List<EnableServiceRequests> enableServiceRequests = enableServiceRequestProxyList
                .stream()
                .map(item->new EnableServiceRequests(item.getServiceId(),
                        item.getServiceName().getDescription(),item.getCompanyName(),
                        item.getStatus(),
                        item.getCreatedDate())).toList();


        return ResponseWrapper.<List<EnableServiceRequests>>builder()
                .data(enableServiceRequests)
                .message("Service request fetched successfully")
                .statusCode(HttpStatus.OK)
                .build();
    }


    private UserProxy getSecurityPrincipal(){
        Optional<UserProxy> userProxyOptional = securityUtilsService.getPrincipal();
        if (userProxyOptional.isEmpty()) throw new AccessDeniedException("Error occurred!");
        return userProxyOptional.get();
    }

    private static ResponseWrapper<CreateProductResponse> mapProductResponse(UUID savedOfferingId,HttpStatus status,String message) {
        return ResponseWrapper.<CreateProductResponse>builder()
                .data(new CreateProductResponse(savedOfferingId))
                .message(message)
                .statusCode(status)
                .build();
    }
}
