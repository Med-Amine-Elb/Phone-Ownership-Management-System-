package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private SystemUser createdBy;

    @Column(nullable = false)
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Converts a Phone entity to a PhoneDto.
     * @param phone The Phone entity.
     * @return The corresponding PhoneDto, or null if the input is null.
     */
    public static com.phone.dto.PhoneDto toDto(Phone phone) {
        if (phone == null) return null;
        com.phone.dto.PhoneDto dto = new com.phone.dto.PhoneDto();
        dto.id = phone.getId();
        dto.brand = phone.getBrand();
        dto.model = phone.getModel();
        dto.active = phone.isActive();
        return dto;
    }

    /**
     * Converts a list of Phone entities to a list of PhoneDtos.
     * @param phones The list of Phone entities.
     * @return The corresponding list of PhoneDtos.
     */
    public static java.util.List<com.phone.dto.PhoneDto> toDtoList(java.util.List<Phone> phones) {
        java.util.List<com.phone.dto.PhoneDto> list = new java.util.ArrayList<>();
        for (Phone p : phones) {
            list.add(toDto(p));
        }
        return list;
    }
} 