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
} 