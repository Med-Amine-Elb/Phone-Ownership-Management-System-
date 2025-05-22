package com.phone.repository;

import com.phone.model.ReminderQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderQueueRepository extends JpaRepository<ReminderQueue, Long> {
    List<ReminderQueue> findByProcessed(boolean processed);
    List<ReminderQueue> findByNotificationSent(boolean notificationSent);
    List<ReminderQueue> findByReminderDateBeforeAndProcessedFalse(LocalDateTime date);
    List<ReminderQueue> findByPhoneAssignmentId(Long phoneAssignmentId);
} 