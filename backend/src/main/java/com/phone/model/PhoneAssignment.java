package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "phone_assignments")
public class PhoneAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "phone_id", nullable = false)
    private Phone phone;

    @ManyToOne
    @JoinColumn(name = "sim_card_id", nullable = false)
    private SimCard simCard;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDateTime acquisitionDate;

    @Column(name = "next_upgrade_date", nullable = false)
    private LocalDateTime nextUpgradeDate;

    @Column(name = "pv_file_url", nullable = false)
    private String pvFileUrl;

    @ManyToOne
    @JoinColumn(name = "assigned_by_user_id", nullable = false)
    private SystemUser assignedBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    public enum AssignmentStatus {
        ACTIVE,
        RETURNED
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = AssignmentStatus.ACTIVE;
        }
        if (acquisitionDate == null) {
            acquisitionDate = LocalDateTime.now();
        }
        if (nextUpgradeDate == null) {
            nextUpgradeDate = acquisitionDate.plusYears(2);
        }
    }
} 