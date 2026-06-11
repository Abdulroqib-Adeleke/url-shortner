package com.jug.url.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CreateCustomerRequest extends CreateUserRequest{

    @NotBlank(message = "please provide company ID")
    @NotNull(message = "please provide company ID")
    private UUID companyId;

}
