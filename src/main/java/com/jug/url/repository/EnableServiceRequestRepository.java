package com.jug.url.repository;

import com.jug.url.dto.proxy.EnableServiceRequestProxy;
import com.jug.url.enums.ServiceRequestStatus;
import com.jug.url.model.EnableServiceRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnableServiceRequestRepository extends JpaRepository<EnableServiceRequestModel, UUID> {

    @Query("SELECT new com.jug.url.dto.proxy.EnableServiceRequestProxy(e.id," +
            "e.serviceId," +
            "p.service," +
            "c.adminId," +
            "c.name," +
            "e.status," +
            "e.createdDate," +
            "e.updatedDate) " +
            "FROM EnableServiceRequestModel e " +
            "JOIN AgentServiceOffering a ON a.id = e.serviceId " +
            "JOIN PrincipalServiceOffering p ON p.id = a.principalServiceId " +
            "JOIN Company c ON c.id = a.companyId " +
            "WHERE e.status = :status")
    List<EnableServiceRequestProxy> fetchPendingServiceRequest(@Param("status") ServiceRequestStatus status);

}
