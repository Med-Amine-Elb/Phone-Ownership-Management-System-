package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "system_users")
public class SystemUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Converts a SystemUser entity to a SystemUserDto.
     * @param user The SystemUser entity.
     * @return The corresponding SystemUserDto, or null if the input is null.
     */
    public static com.phone.dto.SystemUserDto toDto(SystemUser user) {
        if (user == null) return null;
        com.phone.dto.SystemUserDto dto = new com.phone.dto.SystemUserDto();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.role = user.getRole() != null ? user.getRole().getName().name() : null;
        return dto;
    }

    /**
     * Converts a list of SystemUser entities to a list of SystemUserDtos.
     * @param users The list of SystemUser entities.
     * @return The corresponding list of SystemUserDtos.
     */
    public static java.util.List<com.phone.dto.SystemUserDto> toDtoList(java.util.List<SystemUser> users) {
        java.util.List<com.phone.dto.SystemUserDto> list = new java.util.ArrayList<>();
        for (SystemUser u : users) {
            list.add(toDto(u));
        }
        return list;
    }
} 