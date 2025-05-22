package com.phone.controller;

import com.phone.model.Notification;
import com.phone.model.SystemUser;
import com.phone.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getUserNotifications(@RequestAttribute("currentUser") SystemUser currentUser) {
        return notificationService.getUserNotifications(currentUser);
    }

    @GetMapping("/unread")
    public List<Notification> getUnreadUserNotifications(@RequestAttribute("currentUser") SystemUser currentUser) {
        return notificationService.getUnreadUserNotifications(currentUser);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadNotificationCount(@RequestAttribute("currentUser") SystemUser currentUser) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount(currentUser));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }
} 