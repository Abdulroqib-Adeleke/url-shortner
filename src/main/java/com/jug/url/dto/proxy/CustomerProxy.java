package com.jug.url.dto.proxy;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerProxy {
    private UUID id;
    private String name;
    private String email;
    private UUID userId;
    private UUID companyId;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    private String companyName;

    public CustomerProxy(String name, UUID id, String email, UUID userId, UUID companyId,
                         LocalDateTime createdDate,
                         LocalDateTime updatedDate,String companyName) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.userId = userId;
        this.companyId = companyId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.companyName = companyName;
    }
}
