package com.jug.url.dto.proxy;

import com.jug.url.enums.ServiceOffering;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PrincipalServiceProxy {
    private UUID id;
    private ServiceOffering service;
    private boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public PrincipalServiceProxy(UUID id, ServiceOffering service,
                                 boolean active, LocalDateTime createdDate,
                                 LocalDateTime updatedDate) {
        this.id = id;
        this.service = service;
        this.active = active;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
