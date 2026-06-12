package com.jug.url.repository;

import com.jug.url.dto.proxy.UserProxy;
import com.jug.url.enums.Roles;
import com.jug.url.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserModelRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByEmail(String Email);
    Optional<UserModel> findById(UUID id);

    @Query("SELECT new com.jug.url.dto.proxy.UserProxy(u.id,u.name," +
            "u.password," +
            "u.email," +
            "u.createdDate," +
            "u.updatedDate," +
            "u.userType) FROM UserModel u WHERE u.email = :email")
    Optional<UserProxy>findUserByEmail(@Param("email") String email);

    @Query("SELECT new com.jug.url.dto.proxy.UserProxy(u.id,u.name," +
            "u.password," +
            "u.email," +
            "u.createdDate," +
            "u.updatedDate," +
            "u.userType) FROM UserModel u WHERE u.id = :id")
    Optional<UserProxy>findUserById(@Param("id") UUID id);

    @Query("SELECT u.roles FROM UserModel u WHERE u.id = :id")
    Set<Roles> getUserRoles(@Param("id") UUID id);
}
