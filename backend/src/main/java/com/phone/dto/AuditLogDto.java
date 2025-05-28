package com.phone.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for AuditLog entities.
 * Used to expose audit log data.
 */
public class AuditLogDto {
    /** The unique identifier of the audit log entry. */
    public Long id;
    /** The action performed. */
    public String action;
    /** The type of the entity affected by the action. */
    public String entityType;
    /** The ID of the entity affected by the action. */
    public Long entityId;
    /** The system user who performed the action. */
    public SimpleSystemUserDto performedBy;
    /** The timestamp of the action. */
    public LocalDateTime timestamp;

    /**
     * Simple Data Transfer Object for SystemUser entities within AuditLogDto.
     */
    public static class SimpleSystemUserDto {
        /** The unique identifier of the system user. */
        public Long id;
        /** The username of the system user. */
        public String username;
    }
} 