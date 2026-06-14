package com.jug.url.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "expiry_date")
    private Instant expiryDate;

}
