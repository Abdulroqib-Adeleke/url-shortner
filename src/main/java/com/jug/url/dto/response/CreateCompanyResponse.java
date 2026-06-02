package com.jug.url.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCompanyResponse {
    @NotNull(message = "id must be provided")
    private String id;
}
