package com.jug.url.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerProfile(UUID id,
                              String name,
                              String email,
                              UUID userId,
                              UUID companyId,
                              LocalDateTime createdDate,
                              LocalDateTime updatedDate,
                              String companyName,
                              UUID adminId) {
}
