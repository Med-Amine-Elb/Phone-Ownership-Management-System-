package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sim_cards")
public class SimCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private String ssid;

    @Column(name = "pin_code", nullable = false)
    private String pinCode;

    @Column(name = "puk_code", nullable = false)
    private String pukCode;

    @Column(nullable = false)
    private String forfait;

    @Column(nullable = false)
    private String operator;

    @Column(name = "is_assigned", nullable = false)
    private boolean isAssigned = false;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private SystemUser createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Converts a SimCard entity to a SimCardDto.
     * @param simCard The SimCard entity.
     * @return The corresponding SimCardDto, or null if the input is null.
     */
    public static com.phone.dto.SimCardDto toDto(SimCard simCard) {
        if (simCard == null) return null;
        com.phone.dto.SimCardDto dto = new com.phone.dto.SimCardDto();
        dto.id = simCard.getId();
        dto.number = simCard.getNumber();
        dto.operator = simCard.getOperator();
        dto.forfait = simCard.getForfait();
        dto.assigned = simCard.isAssigned();
        return dto;
    }

    /**
     * Converts a list of SimCard entities to a list of SimCardDtos.
     * @param simCards The list of SimCard entities.
     * @return The corresponding list of SimCardDtos.
     */
    public static java.util.List<com.phone.dto.SimCardDto> toDtoList(java.util.List<SimCard> simCards) {
        java.util.List<com.phone.dto.SimCardDto> list = new java.util.ArrayList<>();
        for (SimCard s : simCards) {
            list.add(toDto(s));
        }
        return list;
    }
} 