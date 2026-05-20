package com.jug.url.dto.response;

import com.jug.url.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
public class ProfileResponse {

    private String message;
    private String name;
    private String email;
    private Set<Roles> roles;
}
