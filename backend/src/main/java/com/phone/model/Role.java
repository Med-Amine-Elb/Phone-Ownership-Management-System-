package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    @Column(nullable = false)
    private boolean deleted = false;

    public enum RoleType {
        ADMIN,
        ASSIGNER,
        TECHNICIAN
    }

    /**
     * Converts a Role entity to a RoleDto.
     * @param role The Role entity.
     * @return The corresponding RoleDto, or null if the input is null.
     */
    public static com.phone.dto.RoleDto toDto(Role role) {
        if (role == null) return null;
        com.phone.dto.RoleDto dto = new com.phone.dto.RoleDto();
        dto.id = role.getId();
        dto.name = role.getName() != null ? role.getName().name() : null;
        return dto;
    }

    /**
     * Converts a list of Role entities to a list of RoleDtos.
     * @param roles The list of Role entities.
     * @return The corresponding list of RoleDtos.
     */
    public static java.util.List<com.phone.dto.RoleDto> toDtoList(java.util.List<Role> roles) {
        java.util.List<com.phone.dto.RoleDto> list = new java.util.ArrayList<>();
        for (Role r : roles) {
            list.add(toDto(r));
        }
        return list;
    }
} 