package com.jug.url.dto.proxy;

import com.jug.url.enums.ServiceOffering;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AgentServiceOfferingProxy {
    private UUID id;
    private UUID principalServiceId;
    private boolean active;
    private UUID companyId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean principalServiceActive;
    private ServiceOffering service;

    public AgentServiceOfferingProxy(UUID id, UUID principalServiceId, boolean active,
                                     UUID companyId, LocalDateTime createdDate,
                                     LocalDateTime updatedDate,
                                     boolean principalServiceActive,
                                     ServiceOffering service) {
        this.id = id;
        this.principalServiceId = principalServiceId;
        this.active = active;
        this.companyId = companyId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.principalServiceActive = principalServiceActive;
        this.service = service;
    }
}
