package com.jug.url.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_login_session")
public class UserLoginSession {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "active_session_id")
    private String activeSessionId;
    @Column(name = "user_id")
    private UUID userId;

    @CreationTimestamp
    @Column(name = "created_date")
    LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    LocalDateTime updatedDate;
}
