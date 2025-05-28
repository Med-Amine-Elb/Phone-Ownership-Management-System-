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

    /**
     * Converts an AuditLog entity to an AuditLogDto.
     * @param auditLog The AuditLog entity.
     * @return The corresponding AuditLogDto, or null if the input is null.
     */
    public static com.phone.dto.AuditLogDto toDto(AuditLog auditLog) {
        if (auditLog == null) return null;
        com.phone.dto.AuditLogDto dto = new com.phone.dto.AuditLogDto();
        dto.id = auditLog.getId();
        dto.action = auditLog.getAction() != null ? auditLog.getAction().name() : null;
        dto.entityType = auditLog.getEntityType();
        dto.entityId = auditLog.getEntityId();
        if (auditLog.getPerformedBy() != null) {
            com.phone.dto.AuditLogDto.SimpleSystemUserDto userDto = new com.phone.dto.AuditLogDto.SimpleSystemUserDto();
            userDto.id = auditLog.getPerformedBy().getId();
            userDto.username = auditLog.getPerformedBy().getUsername();
            dto.performedBy = userDto;
        }
        dto.timestamp = auditLog.getTimestamp();
        return dto;
    }

    /**
     * Converts a list of AuditLog entities to a list of AuditLogDtos.
     * @param auditLogs The list of AuditLog entities.
     * @return The corresponding list of AuditLogDtos.
     */
    public static java.util.List<com.phone.dto.AuditLogDto> toDtoList(java.util.List<AuditLog> auditLogs) {
        java.util.List<com.phone.dto.AuditLogDto> list = new java.util.ArrayList<>();
        for (AuditLog al : auditLogs) {
            list.add(toDto(al));
        }
        return list;
    }
} 