package com.jug.url.repository;

import com.jug.url.model.RefreshToken;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(UUID userId);

    Optional<RefreshToken> findByUserId(@NotNull(message = "provide a valid user id") UUID userId);
}
