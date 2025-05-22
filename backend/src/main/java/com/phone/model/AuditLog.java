package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType action;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @ManyToOne
    @JoinColumn(name = "performed_by", nullable = false)
    private SystemUser performedBy;

    @Column(name = "role_at_time", nullable = false)
    private String roleAtTime;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public enum ActionType {
        ADD_SIM,
        ASSIGN_PHONE,
        UPLOAD_PV,
        RETURN_PHONE,
        CREATE_USER,
        UPDATE_USER,
        DELETE_USER,
        CREATE_PHONE,
        UPDATE_PHONE,
        DELETE_PHONE
    }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
} 