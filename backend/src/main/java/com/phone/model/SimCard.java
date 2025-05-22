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

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private SystemUser createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 