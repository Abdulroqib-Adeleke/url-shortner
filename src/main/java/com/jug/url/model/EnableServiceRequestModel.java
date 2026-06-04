package com.jug.url.model;

import com.jug.url.enums.ServiceRequestStatus;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "enable_service_request")
public class EnableServiceRequestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "service_id")
    private UUID serviceId;
    @Column(name = "admin_id")
    private UUID adminId;
    @Column(name = "service_request_status")
    @Enumerated(EnumType.STRING)
    private ServiceRequestStatus status;
    @CreationTimestamp
    @Column(name = "created_date")
    LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    LocalDateTime updatedDate;
}

