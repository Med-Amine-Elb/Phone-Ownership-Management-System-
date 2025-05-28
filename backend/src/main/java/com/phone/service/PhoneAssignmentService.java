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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public Page<PhoneAssignment> getAssignments(Pageable pageable, String filterField, String filterValue) {
        if (filterField != null && filterValue != null && !filterValue.isEmpty()) {
            switch (filterField) {
                case "user":
                    return phoneAssignmentRepository.findByUserIdAndDeletedFalse(Long.parseLong(filterValue), pageable);
                case "phone":
                    return phoneAssignmentRepository.findByPhoneIdAndDeletedFalse(Long.parseLong(filterValue), pageable);
                case "simCard":
                    return phoneAssignmentRepository.findBySimCardIdAndDeletedFalse(Long.parseLong(filterValue), pageable);
                case "status":
                    try {
                        PhoneAssignment.AssignmentStatus status = PhoneAssignment.AssignmentStatus.valueOf(filterValue.toUpperCase());
                        return phoneAssignmentRepository.findByStatusAndDeletedFalse(status, pageable);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid status value
                        return Page.empty(pageable);
                    }
                // Add more filter cases as needed
                default:
                    // No valid filter field provided, return all non-deleted assignments with pagination/sorting
                    return phoneAssignmentRepository.findByDeletedFalse(pageable);
            }
        } else {
            // No filter provided, return all non-deleted assignments with pagination/sorting
            return phoneAssignmentRepository.findByDeletedFalse(pageable);
        }
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

    public PhoneAssignment updateAssignment(Long id, PhoneAssignment assignment) {
        PhoneAssignment existing = phoneAssignmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Update fields as needed
        existing.setPhone(assignment.getPhone());
        existing.setSimCard(assignment.getSimCard());
        existing.setUser(assignment.getUser());
        existing.setPvFileUrl(assignment.getPvFileUrl());
        existing.setStatus(assignment.getStatus());
        if (assignment.getStatus() == PhoneAssignment.AssignmentStatus.RETURNED) {
            existing.setReturnedDate(LocalDateTime.now());
        }
        // Add more fields if needed

        return phoneAssignmentRepository.save(existing);
    }

    public PhoneAssignment softDelete(Long id) {
        return phoneAssignmentRepository.findById(id)
                .map(assignment -> {
                    assignment.setDeleted(true);
                    return phoneAssignmentRepository.save(assignment);
                })
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
    }

    public static com.phone.dto.PhoneAssignmentDto toDto(com.phone.model.PhoneAssignment assignment) {
        if (assignment == null) return null;
        com.phone.dto.PhoneAssignmentDto dto = new com.phone.dto.PhoneAssignmentDto();
        dto.id = assignment.getId();
        // User
        if (assignment.getUser() != null) {
            com.phone.dto.PhoneAssignmentDto.SimpleUserDto userDto = new com.phone.dto.PhoneAssignmentDto.SimpleUserDto();
            userDto.id = assignment.getUser().getId();
            userDto.firstName = assignment.getUser().getFirstName();
            userDto.lastName = assignment.getUser().getLastName();
            userDto.department = assignment.getUser().getDepartment();
            userDto.status = assignment.getUser().getStatus() != null ? assignment.getUser().getStatus().name() : null;
            dto.user = userDto;
        }
        // Phone
        if (assignment.getPhone() != null) {
            com.phone.dto.PhoneAssignmentDto.SimplePhoneDto phoneDto = new com.phone.dto.PhoneAssignmentDto.SimplePhoneDto();
            phoneDto.id = assignment.getPhone().getId();
            phoneDto.brand = assignment.getPhone().getBrand();
            phoneDto.model = assignment.getPhone().getModel();
            phoneDto.active = assignment.getPhone().isActive();
            dto.phone = phoneDto;
        }
        // SimCard
        if (assignment.getSimCard() != null) {
            com.phone.dto.PhoneAssignmentDto.SimpleSimCardDto simDto = new com.phone.dto.PhoneAssignmentDto.SimpleSimCardDto();
            simDto.id = assignment.getSimCard().getId();
            simDto.number = assignment.getSimCard().getNumber();
            simDto.operator = assignment.getSimCard().getOperator();
            simDto.forfait = assignment.getSimCard().getForfait();
            simDto.assigned = assignment.getSimCard().isAssigned();
            dto.simCard = simDto;
        }
        // AssignedBy
        if (assignment.getAssignedBy() != null) {
            com.phone.dto.PhoneAssignmentDto.SimpleSystemUserDto sysUserDto = new com.phone.dto.PhoneAssignmentDto.SimpleSystemUserDto();
            sysUserDto.id = assignment.getAssignedBy().getId();
            sysUserDto.username = assignment.getAssignedBy().getUsername();
            sysUserDto.role = assignment.getAssignedBy().getRole() != null ? assignment.getAssignedBy().getRole().getName().name() : null;
            dto.assignedBy = sysUserDto;
        }
        dto.acquisitionDate = assignment.getAcquisitionDate();
        dto.nextUpgradeDate = assignment.getNextUpgradeDate();
        dto.pvFileUrl = assignment.getPvFileUrl();
        dto.status = assignment.getStatus() != null ? assignment.getStatus().name() : null;
        dto.returnedDate = assignment.getReturnedDate();
        return dto;
    }

    public static java.util.List<com.phone.dto.PhoneAssignmentDto> toDtoList(java.util.List<com.phone.model.PhoneAssignment> assignments) {
        java.util.List<com.phone.dto.PhoneAssignmentDto> list = new java.util.ArrayList<>();
        for (com.phone.model.PhoneAssignment a : assignments) {
            list.add(toDto(a));
        }
        return list;
    }
} 