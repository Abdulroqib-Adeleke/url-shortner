package com.jug.url.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Table(name = "user_login_session")
public class UserLoginSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "active_session_id")
    private String activeSessionId;

    @Column(name = "user_id")
    private UUID userId;

    //private String refreshToken;

    @CreationTimestamp
    @Column(name = "created_date")
    LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    LocalDateTime updatedDate;
}
