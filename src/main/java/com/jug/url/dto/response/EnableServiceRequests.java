package com.jug.url.dto.response;

import com.jug.url.enums.ServiceRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EnableServiceRequests {
    private UUID serviceId;
    private String serviceName;
    private String companyName;
    private ServiceRequestStatus status;
    LocalDateTime createdDate;
}
