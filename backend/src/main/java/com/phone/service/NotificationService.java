package com.phone.service;

import com.phone.model.*;
import com.phone.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PhoneAssignmentService phoneAssignmentService;

    public void createUpgradeNotification(PhoneAssignment assignment) {
        String message = String.format(
            "Rappel de mise à niveau : Le téléphone (ID: %d) de %s %s sera éligible pour une mise à niveau le %s.",
            assignment.getPhone().getId(),
            assignment.getUser().getFirstName(),
            assignment.getUser().getLastName(),
            assignment.getNextUpgradeDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );

        // Create notification for admin
        Notification adminNotification = new Notification();
        adminNotification.setMessage(message);
        adminNotification.setCreatedAt(LocalDateTime.now());
        adminNotification.setRead(false);
        adminNotification.setType(Notification.NotificationType.UPGRADE_REMINDER);
        adminNotification.setPhoneAssignment(assignment);
        notificationRepository.save(adminNotification);

        // Create notification for assigner
        if (assignment.getAssignedBy() != null) {
            Notification assignerNotification = new Notification();
            assignerNotification.setUser(assignment.getAssignedBy());
            assignerNotification.setMessage(message);
            assignerNotification.setCreatedAt(LocalDateTime.now());
            assignerNotification.setRead(false);
            assignerNotification.setType(Notification.NotificationType.UPGRADE_REMINDER);
            assignerNotification.setPhoneAssignment(assignment);
            notificationRepository.save(assignerNotification);
        }
    }

    public void createAssignmentNotification(PhoneAssignment assignment) {
        String message = String.format(
            "Nouvelle attribution : Le téléphone (ID: %d) a été attribué à %s %s.",
            assignment.getPhone().getId(),
            assignment.getUser().getFirstName(),
            assignment.getUser().getLastName()
        );

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(Notification.NotificationType.ASSIGNMENT_CREATED);
        notification.setPhoneAssignment(assignment);
        notificationRepository.save(notification);
    }

    public void createReturnNotification(PhoneAssignment assignment) {
        String message = String.format(
            "Retour de téléphone : Le téléphone (ID: %d) a été retourné par %s %s.",
            assignment.getPhone().getId(),
            assignment.getUser().getFirstName(),
            assignment.getUser().getLastName()
        );

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(Notification.NotificationType.ASSIGNMENT_RETURNED);
        notification.setPhoneAssignment(assignment);
        notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(SystemUser user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Notification> getUnreadUserNotifications(SystemUser user) {
        return notificationRepository.findByUserAndReadOrderByCreatedAtDesc(user, false);
    }

    public long getUnreadNotificationCount(SystemUser user) {
        return notificationRepository.countByUserAndRead(user, false);
    }

    public void markNotificationAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public void processUpgradeNotifications() {
        List<PhoneAssignment> upcomingUpgrades = phoneAssignmentService.getUpcomingUpgrades();
        for (PhoneAssignment assignment : upcomingUpgrades) {
            createUpgradeNotification(assignment);
        }
    }
} 