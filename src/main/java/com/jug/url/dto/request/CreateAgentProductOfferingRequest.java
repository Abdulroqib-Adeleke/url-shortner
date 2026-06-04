package com.jug.url.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateAgentProductOfferingRequest {
    @NotBlank
    private UUID companyId;
    @NotBlank
    private UUID serviceOfferingId;
}

