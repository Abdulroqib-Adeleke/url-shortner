package com.jug.url.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCompanyRequest {

    @Email(message = "please provide a valid mail")
    private String email;
    @NotBlank(message = "please provide company name")
    private String companyName;
}
