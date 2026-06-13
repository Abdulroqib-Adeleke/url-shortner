package com.jug.url.repository;

import com.jug.url.dto.proxy.CustomerProxy;
import com.jug.url.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, UUID> {

    @Query("SELECT new com.jug.url.dto.proxy.CustomerProxy(c.name," +
            "c.id," +
            "c.email," +
            "c.userId," +
            "c.companyId," +
            "c.createdDate," +
            "c.updatedDate," +
            "comp.name," +
            "comp.adminId)FROM CustomerModel c JOIN Company comp ON comp.id = c.companyId WHERE c.id = :id")
    Optional<CustomerProxy> findCustomerById(@Param("id") UUID id);

    @Query("SELECT new com.jug.url.dto.proxy.CustomerProxy(c.name," +
            "c.id," +
            "c.email," +
            "c.userId," +
            "c.companyId," +
            "c.createdDate," +
            "c.updatedDate," +
            "comp.name," +
            "comp.adminId)FROM CustomerModel c JOIN Company comp ON comp.id = c.companyId WHERE c.userId = :userId")
    Optional<CustomerProxy> findCustomerByUserId(@Param("userId") UUID userId);

    @Query("SELECT new com.jug.url.dto.proxy.CustomerProxy(c.name," +
            "c.id," +
            "c.email," +
            "c.userId," +
            "c.companyId," +
            "c.createdDate," +
            "c.updatedDate," +
            "comp.name," +
            "comp.adminId)FROM CustomerModel c JOIN Company comp ON comp.id = c.companyId " +
            "WHERE c.email = :email AND c.companyId = :companyId")
    Optional<CustomerProxy> findCustomerByEmailAndCompanyId(@Param("email") String email, @Param("companyId") UUID companyId);

}
