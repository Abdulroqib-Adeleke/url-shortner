package com.jug.url.repository;

import com.jug.url.dto.response.CompanyProfile;
import com.jug.url.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByName(String name);
    boolean existsByAdminId(UUID adminId);

    @Query("SELECT new com.jug.url.dto.response.CompanyProfile(" +
            "c.id," +
            "u.name,c.adminId," +
            "c.name,c.supportEmail," +
            "c.createdDate," +
            "c.updatedDate," +
            "c.baseUrl) FROM Company c JOIN UserModel u ON u.id = c.adminId WHERE c.adminId = :adminId")
    Optional<CompanyProfile> fetchCompanyProfile(@Param("adminId") UUID adminId);

    @Query("SELECT new com.jug.url.dto.response.CompanyProfile(" +
            "c.id, " +
            "u.name," +
            "c.adminId," +
            "c.name," +
            "c.supportEmail, " +
            "c.createdDate, " +
            "c.updatedDate," +
            "c.baseUrl)FROM Company  c JOIN UserModel u ON u.id = c.adminId WHERE c.id = :id")
    Optional<CompanyProfile> fetchCompanyProfileById(@Param("id") UUID id);
}
