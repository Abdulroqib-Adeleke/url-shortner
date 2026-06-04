package com.jug.url.dto.proxy;

import com.jug.url.enums.ServiceOffering;
import com.jug.url.enums.ServiceRequestStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EnableServiceRequestProxy {
    private UUID id; // enable service request id
    private UUID serviceId; //service id
    private ServiceOffering serviceName;
    private UUID adminId;
    private String companyName;
    private ServiceRequestStatus status;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;

    public EnableServiceRequestProxy(UUID id, UUID serviceId, ServiceOffering serviceName,
                                     UUID adminId, String companyName, ServiceRequestStatus status,
                                     LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.adminId = adminId;
        this.companyName = companyName;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
