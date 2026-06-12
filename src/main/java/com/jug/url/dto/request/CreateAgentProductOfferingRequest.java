package com.jug.url.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateAgentProductOfferingRequest {

    @NotNull
    private UUID companyId;
    @NotNull
    private UUID serviceOfferingId;
}

