package com.phone.repository;

import com.phone.model.PhoneAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PhoneAssignmentRepository extends JpaRepository<PhoneAssignment, Long> {
    List<PhoneAssignment> findByUserId(Long userId);
    List<PhoneAssignment> findByPhoneId(Long phoneId);
    List<PhoneAssignment> findBySimCardId(Long simCardId);
    List<PhoneAssignment> findByStatus(PhoneAssignment.AssignmentStatus status);
    List<PhoneAssignment> findByNextUpgradeDateBefore(LocalDateTime date);
    List<PhoneAssignment> findByAssignedById(Long assignedById);

    // Methods for pagination, sorting, and filtering
    Page<PhoneAssignment> findByDeletedFalse(Pageable pageable);
    Page<PhoneAssignment> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);
    Page<PhoneAssignment> findByPhoneIdAndDeletedFalse(Long phoneId, Pageable pageable);
    Page<PhoneAssignment> findBySimCardIdAndDeletedFalse(Long simCardId, Pageable pageable);
    Page<PhoneAssignment> findByStatusAndDeletedFalse(PhoneAssignment.AssignmentStatus status, Pageable pageable);

    List<PhoneAssignment> findByDeletedFalse();

    // Method to find assignment by ID, ignoring the deleted status
    Optional<PhoneAssignment> findById(Long id);
} 