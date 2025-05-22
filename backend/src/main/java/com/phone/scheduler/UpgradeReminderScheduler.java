package com.phone.scheduler;

import com.phone.model.PhoneAssignment;
import com.phone.model.ReminderQueue;
import com.phone.repository.PhoneAssignmentRepository;
import com.phone.repository.ReminderQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class UpgradeReminderScheduler {

    @Autowired
    private PhoneAssignmentRepository phoneAssignmentRepository;

    @Autowired
    private ReminderQueueRepository reminderQueueRepository;

    @Scheduled(cron = "0 0 9 * * ?") // Run at 9 AM every day
    public void checkUpcomingUpgrades() {
        LocalDateTime twoMonthsFromNow = LocalDateTime.now().plusMonths(2);
        List<PhoneAssignment> upcomingUpgrades = phoneAssignmentRepository.findByNextUpgradeDateBefore(twoMonthsFromNow);

        for (PhoneAssignment assignment : upcomingUpgrades) {
            // Check if a reminder already exists
            if (reminderQueueRepository.findByPhoneAssignmentId(assignment.getId()).isEmpty()) {
                ReminderQueue reminder = new ReminderQueue();
                reminder.setPhoneAssignment(assignment);
                reminder.setReminderDate(LocalDateTime.now());
                reminderQueueRepository.save(reminder);
            }
        }
    }

    @Scheduled(cron = "0 0 10 * * ?") // Run at 10 AM every day
    public void processReminders() {
        List<ReminderQueue> unprocessedReminders = reminderQueueRepository.findByReminderDateBeforeAndProcessedFalse(LocalDateTime.now());

        for (ReminderQueue reminder : unprocessedReminders) {
            // TODO: Implement notification sending logic
            // For now, just mark as processed
            reminder.setProcessed(true);
            reminder.setProcessedAt(LocalDateTime.now());
            reminderQueueRepository.save(reminder);
        }
    }
} 