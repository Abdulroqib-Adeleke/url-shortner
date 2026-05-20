package com.jug.url.dto.request;

import com.jug.url.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @NotNull
    @NotEmpty(message = "provide a name")
    private String name;
    @Email(message = "provide a valid email")
    private String email;
    private String password;
    private Set<Roles> roles;

}
