package com.jug.url.repository;

import com.jug.url.dto.proxy.AgentServiceOfferingProxy;
import com.jug.url.model.AgentServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentServiceOfferingRepository extends JpaRepository<AgentServiceOffering, UUID> {
    @Override
    Optional<AgentServiceOffering> findById(@Param("id") UUID uuid);

    @Override
    boolean existsById(UUID uuid);

    @Query("SELECT new com.jug.url.dto.proxy.AgentServiceOfferingProxy(a.id," +
            "a.principalServiceId," +
            "a.active,a.companyId," +
            "a.createdDate," +
            "a.updatedDate," +
            "p.active," +
            "p.service) FROM AgentServiceOffering a " +
            "JOIN PrincipalServiceOffering p ON p.id = a.principalServiceId " +
            "JOIN Company c ON c.id = a.companyId " +
            "WHERE p.active = true " +
            "AND a.active = true " +
            "AND c.adminId = :adminId")
    List<AgentServiceOfferingProxy> getAgentActiveServices(@Param("adminId") UUID adminId);

    @Query("SELECT new com.jug.url.dto.proxy.AgentServiceOfferingProxy(a.id," +
            "a.principalServiceId," +
            "a.active," +
            "a.companyId," +
            "a.createdDate," +
            "a.updatedDate," +
            "p.active," +
            "p.service) FROM AgentServiceOffering a " +
            "JOIN PrincipalServiceOffering p ON p.id = a.principalServiceId " +
            "JOIN Company c ON c.id = a.companyId " +
            "WHERE p.active = true " +
            "AND a.active = true " +
            "AND a.companyId = :companyId")
    List<AgentServiceOfferingProxy> getAgentActiveServicesByCompanyId(@Param("companyId") UUID companyId);
}