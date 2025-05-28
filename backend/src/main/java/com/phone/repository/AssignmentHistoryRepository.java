package com.phone.repository;

import com.phone.model.AssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentHistoryRepository extends JpaRepository<AssignmentHistory, Long>, JpaSpecificationExecutor<AssignmentHistory> {
    List<AssignmentHistory> findByUserId(Long userId);
    List<AssignmentHistory> findByPhoneId(Long phoneId);
    List<AssignmentHistory> findBySimCardId(Long simCardId);
    List<AssignmentHistory> findByStatus(AssignmentHistory.HistoryStatus status);
    List<AssignmentHistory> findByAssignedById(Long assignedById);
} 