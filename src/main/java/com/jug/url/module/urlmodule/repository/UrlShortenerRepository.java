package com.jug.url.module.urlmodule.repository;

import com.jug.url.model.UrlShortenerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlShortenerModel, UUID> {

    boolean existsByShortCode(String shortCode);
    Optional<UrlShortenerModel> findByShortCode(String shortCode);
}
