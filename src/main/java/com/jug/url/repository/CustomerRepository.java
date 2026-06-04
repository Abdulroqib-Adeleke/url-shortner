package com.jug.url.repository;

import com.jug.url.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, UUID> {
    Optional<CustomerModel> findByUserId(UUID uuid);
}
