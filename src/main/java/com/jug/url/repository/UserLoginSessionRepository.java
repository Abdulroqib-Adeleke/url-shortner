package com.jug.url.repository;

import com.jug.url.model.UserLoginSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserLoginSessionRepository extends JpaRepository<UserLoginSession, UUID> {

    Optional<UserLoginSession> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);

}
