package com.jug.url.dto.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SavedUserResponse {

    private UUID userId;
    private String token;

}
