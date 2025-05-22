package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "assignment_history")
public class AssignmentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phone_assignment_id")
    private PhoneAssignment phoneAssignment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "sim_card_id", nullable = false)
    private SimCard simCard;

    @ManyToOne
    @JoinColumn(name = "phone_id", nullable = false)
    private Phone phone;

    @ManyToOne
    @JoinColumn(name = "assigned_by_user_id", nullable = false)
    private SystemUser assignedBy;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDateTime acquisitionDate;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryStatus status;

    public enum HistoryStatus {
        RETURNED,
        REASSIGNED
    }

    @PrePersist
    protected void onCreate() {
        if (acquisitionDate == null) {
            acquisitionDate = LocalDateTime.now();
        }
    }
} 