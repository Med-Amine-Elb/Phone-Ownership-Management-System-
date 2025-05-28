package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean deleted = false;

    public enum UserStatus {
        ACTIVE,
        LEFT_COMPANY
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = UserStatus.ACTIVE;
        }
    }

    /**
     * Converts a User entity to a UserDto.
     * @param user The User entity.
     * @return The corresponding UserDto, or null if the input is null.
     */
    public static com.phone.dto.UserDto toDto(User user) {
        if (user == null) return null;
        com.phone.dto.UserDto dto = new com.phone.dto.UserDto();
        dto.id = user.getId();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.department = user.getDepartment();
        dto.status = user.getStatus() != null ? user.getStatus().name() : null;
        return dto;
    }

    /**
     * Converts a list of User entities to a list of UserDtos.
     * @param users The list of User entities.
     * @return The corresponding list of UserDtos.
     */
    public static java.util.List<com.phone.dto.UserDto> toDtoList(java.util.List<User> users) {
        java.util.List<com.phone.dto.UserDto> list = new java.util.ArrayList<>();
        for (User u : users) {
            list.add(toDto(u));
        }
        return list;
    }
} 