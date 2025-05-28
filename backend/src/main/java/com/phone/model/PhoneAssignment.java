package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "phone_assignments")
public class PhoneAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "phone_id", nullable = false)
    private Phone phone;

    @ManyToOne
    @JoinColumn(name = "sim_card_id", nullable = false)
    private SimCard simCard;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDateTime acquisitionDate;

    @Column(name = "next_upgrade_date", nullable = false)
    private LocalDateTime nextUpgradeDate;

    @Column(name = "pv_file_url", nullable = false)
    private String pvFileUrl;

    @ManyToOne
    @JoinColumn(name = "assigned_by_user_id", nullable = false)
    private SystemUser assignedBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    @Column(nullable = false)
    private boolean deleted = false;

    public enum AssignmentStatus {
        ACTIVE,
        RETURNED
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = AssignmentStatus.ACTIVE;
        }
        if (acquisitionDate == null) {
            acquisitionDate = LocalDateTime.now();
        }
        if (nextUpgradeDate == null) {
            nextUpgradeDate = acquisitionDate.plusYears(2);
        }
    }

    /**
     * Converts a PhoneAssignment entity to a PhoneAssignmentDto.
     * @param assignment The PhoneAssignment entity.
     * @return The corresponding PhoneAssignmentDto, or null if the input is null.
     */
    public static com.phone.dto.PhoneAssignmentDto toDto(PhoneAssignment assignment) {
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

    /**
     * Converts a list of PhoneAssignment entities to a list of PhoneAssignmentDtos.
     * @param assignments The list of PhoneAssignment entities.
     * @return The corresponding list of PhoneAssignmentDtos.
     */
    public static java.util.List<com.phone.dto.PhoneAssignmentDto> toDtoList(java.util.List<PhoneAssignment> assignments) {
        java.util.List<com.phone.dto.PhoneAssignmentDto> list = new java.util.ArrayList<>();
        for (PhoneAssignment a : assignments) {
            list.add(toDto(a));
        }
        return list;
    }
} 