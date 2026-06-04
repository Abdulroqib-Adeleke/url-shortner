package com.jug.url.dto.response;

import com.jug.url.enums.ServiceOffering;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ServiceOfferingResponse {
    private ServiceOffering service;
    private String description;
    private UUID serviceId;
}
