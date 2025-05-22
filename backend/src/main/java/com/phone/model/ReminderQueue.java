package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reminder_queue")
public class ReminderQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phone_assignment_id", nullable = false)
    private PhoneAssignment phoneAssignment;

    @Column(name = "reminder_date", nullable = false)
    private LocalDateTime reminderDate;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "notification_sent")
    private boolean notificationSent = false;

    @Column(name = "notification_sent_at")
    private LocalDateTime notificationSentAt;

    @PrePersist
    protected void onCreate() {
        if (reminderDate == null) {
            // Set reminder date to 2 months before next upgrade date
            reminderDate = phoneAssignment.getNextUpgradeDate().minusMonths(2);
        }
    }
} 