package com.jug.url.dto.request;

import com.jug.url.enums.ServiceOffering;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePrincipalProductOfferingRequest {

    @NotNull
    private ServiceOffering serviceOffering;

}
