package com.phone.repository;

import com.phone.model.PhoneAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhoneAssignmentRepository extends JpaRepository<PhoneAssignment, Long> {
    List<PhoneAssignment> findByUserId(Long userId);
    List<PhoneAssignment> findByPhoneId(Long phoneId);
    List<PhoneAssignment> findBySimCardId(Long simCardId);
    List<PhoneAssignment> findByStatus(PhoneAssignment.AssignmentStatus status);
    List<PhoneAssignment> findByNextUpgradeDateBefore(LocalDateTime date);
    List<PhoneAssignment> findByAssignedById(Long assignedById);
} 