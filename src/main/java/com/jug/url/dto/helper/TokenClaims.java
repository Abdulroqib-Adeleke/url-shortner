package com.jug.url.dto.helper;


import com.jug.url.enums.Roles;
import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class TokenClaims {
    private String username;
    private String userSessionId;
    private String tenancyId;
    private Set<Roles> roles;
}
