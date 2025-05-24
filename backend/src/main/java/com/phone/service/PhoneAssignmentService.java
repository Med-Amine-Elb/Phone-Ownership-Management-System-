package com.phone.service;

import com.phone.model.*;
import com.phone.repository.PhoneAssignmentRepository;
import com.phone.repository.AssignmentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PhoneAssignmentService {
    @Autowired
    private PhoneAssignmentRepository phoneAssignmentRepository;

    @Autowired
    private AssignmentHistoryRepository assignmentHistoryRepository;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private SimCardService simCardService;

    public List<PhoneAssignment> getAllAssignments() {
        return phoneAssignmentRepository.findAll();
    }

    public Optional<PhoneAssignment> getAssignmentById(Long id) {
        return phoneAssignmentRepository.findById(id);
    }

    public List<PhoneAssignment> getAssignmentsByUserId(Long userId) {
        return phoneAssignmentRepository.findByUserId(userId);
    }

    public List<PhoneAssignment> getAssignmentsByPhoneId(Long phoneId) {
        return phoneAssignmentRepository.findByPhoneId(phoneId);
    }

    public List<PhoneAssignment> getAssignmentsBySimCardId(Long simCardId) {
        return phoneAssignmentRepository.findBySimCardId(simCardId);
    }

    public List<PhoneAssignment> getActiveAssignments() {
        return phoneAssignmentRepository.findByStatus(PhoneAssignment.AssignmentStatus.ACTIVE);
    }

    public List<PhoneAssignment> getReturnedAssignments() {
        return phoneAssignmentRepository.findByStatus(PhoneAssignment.AssignmentStatus.RETURNED);
    }

    public PhoneAssignment createAssignment(PhoneAssignment assignment) {
        // Validate phone and SIM card availability
        Phone phone = phoneService.getPhoneById(assignment.getPhone().getId())
                .orElseThrow(() -> new RuntimeException("Phone not found"));
        SimCard simCard = simCardService.getSimCardById(assignment.getSimCard().getId())
                .orElseThrow(() -> new RuntimeException("SIM card not found"));

        if (!phone.isActive()) {
            throw new RuntimeException("Phone is not active");
        }
        if (simCard.isAssigned()) {
            throw new RuntimeException("SIM card is already assigned");
        }

        // Set assignment details
        assignment.setStatus(PhoneAssignment.AssignmentStatus.ACTIVE);
        assignment.setAcquisitionDate(LocalDateTime.now());
        assignment.setNextUpgradeDate(assignment.getAcquisitionDate().plusYears(2));

        // Mark SIM card as assigned
        simCardService.markAsAssigned(simCard.getId());

        PhoneAssignment saved = phoneAssignmentRepository.save(assignment);
        return phoneAssignmentRepository.findById(saved.getId()).get();
    }

    public PhoneAssignment returnAssignment(Long id) {
        return phoneAssignmentRepository.findById(id)
                .map(assignment -> {
                    // Create history record
                    AssignmentHistory history = new AssignmentHistory();
                    history.setPhoneAssignment(assignment);
                    history.setUser(assignment.getUser());
                    history.setPhone(assignment.getPhone());
                    history.setSimCard(assignment.getSimCard());
                    history.setAssignedBy(assignment.getAssignedBy());
                    history.setAcquisitionDate(assignment.getAcquisitionDate());
                    history.setReturnedDate(LocalDateTime.now());
                    history.setStatus(AssignmentHistory.HistoryStatus.RETURNED);
                    assignmentHistoryRepository.save(history);

                    // Update assignment
                    assignment.setStatus(PhoneAssignment.AssignmentStatus.RETURNED);
                    assignment.setReturnedDate(LocalDateTime.now());

                    // Mark SIM card as unassigned
                    simCardService.markAsUnassigned(assignment.getSimCard().getId());

                    return phoneAssignmentRepository.save(assignment);
                })
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
    }

    public List<PhoneAssignment> getUpcomingUpgrades() {
        LocalDateTime twoMonthsFromNow = LocalDateTime.now().plusMonths(2);
        return phoneAssignmentRepository.findByNextUpgradeDateBefore(twoMonthsFromNow);
    }
} 