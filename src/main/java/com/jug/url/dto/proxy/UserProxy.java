package com.jug.url.dto.proxy;

import com.jug.url.enums.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserProxy {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private Set<Roles> roles;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;



    public UserProxy(UUID id, String name,
                     String password, String email,
                     LocalDateTime createdDate,
                     LocalDateTime updatedDate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}