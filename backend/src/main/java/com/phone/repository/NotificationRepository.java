package com.phone.repository;

import com.phone.model.Notification;
import com.phone.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(SystemUser user);
    List<Notification> findByUserAndReadOrderByCreatedAtDesc(SystemUser user, boolean read);
    long countByUserAndRead(SystemUser user, boolean read);
} 