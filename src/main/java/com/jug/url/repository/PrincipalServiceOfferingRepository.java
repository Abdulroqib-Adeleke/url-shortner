package com.jug.url.repository;

import com.jug.url.dto.proxy.PrincipalServiceProxy;
import com.jug.url.model.PrincipalServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrincipalServiceOfferingRepository extends JpaRepository<PrincipalServiceOffering, UUID> {
    @Query("SELECT new com.jug.url.dto.proxy.PrincipalServiceProxy(p.id,p.service,p.active,p.createdDate,p.updatedDate) FROM PrincipalServiceOffering p WHERE p.active = true")
    public List<PrincipalServiceProxy> fetchImplementedProducts();

    Optional<PrincipalServiceOffering> findById(UUID id);
}
