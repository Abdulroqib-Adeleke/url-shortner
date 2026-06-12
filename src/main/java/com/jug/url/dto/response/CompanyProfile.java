package com.jug.url.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CompanyProfile {

    private UUID id;
    private String adminName;
    private UUID adminId;
    private String name;
    private String supportEmail;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<ServiceOfferingResponse> modules;
    private String baseUrl;

    public CompanyProfile(UUID id, String adminName, UUID adminId,
                          String name, String supportEmail,
                          LocalDateTime createdDate, LocalDateTime updatedDate,
                          String baseUrl) {
        this.id = id;
        this.adminName = adminName;
        this.adminId = adminId;
        this.name = name;
        this.supportEmail = supportEmail;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.baseUrl = baseUrl;
    }

}