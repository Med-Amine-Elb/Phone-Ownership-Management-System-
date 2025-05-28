package com.phone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "assignment_history")
public class AssignmentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phone_assignment_id")
    private PhoneAssignment phoneAssignment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "sim_card_id", nullable = false)
    private SimCard simCard;

    @ManyToOne
    @JoinColumn(name = "phone_id", nullable = false)
    private Phone phone;

    @ManyToOne
    @JoinColumn(name = "assigned_by_user_id", nullable = false)
    private SystemUser assignedBy;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDateTime acquisitionDate;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryStatus status;

    public enum HistoryStatus {
        RETURNED,
        REASSIGNED
    }

    @PrePersist
    protected void onCreate() {
        if (acquisitionDate == null) {
            acquisitionDate = LocalDateTime.now();
        }
    }

    /**
     * Converts an AssignmentHistory entity to an AssignmentHistoryDto.
     * @param history The AssignmentHistory entity.
     * @return The corresponding AssignmentHistoryDto, or null if the input is null.
     */
    public static com.phone.dto.AssignmentHistoryDto toDto(AssignmentHistory history) {
        if (history == null) return null;
        com.phone.dto.AssignmentHistoryDto dto = new com.phone.dto.AssignmentHistoryDto();
        dto.id = history.getId();
        dto.phoneAssignmentId = history.getPhoneAssignment() != null ? history.getPhoneAssignment().getId() : null;
        
        if (history.getUser() != null) {
            com.phone.dto.AssignmentHistoryDto.SimpleUserDto userDto = new com.phone.dto.AssignmentHistoryDto.SimpleUserDto();
            userDto.id = history.getUser().getId();
            userDto.firstName = history.getUser().getFirstName();
            userDto.lastName = history.getUser().getLastName();
            dto.user = userDto;
        }

        if (history.getPhone() != null) {
            com.phone.dto.AssignmentHistoryDto.SimplePhoneDto phoneDto = new com.phone.dto.AssignmentHistoryDto.SimplePhoneDto();
            phoneDto.id = history.getPhone().getId();
            phoneDto.brand = history.getPhone().getBrand();
            phoneDto.model = history.getPhone().getModel();
            dto.phone = phoneDto;
        }

        if (history.getSimCard() != null) {
            com.phone.dto.AssignmentHistoryDto.SimpleSimCardDto simDto = new com.phone.dto.AssignmentHistoryDto.SimpleSimCardDto();
            simDto.id = history.getSimCard().getId();
            simDto.number = history.getSimCard().getNumber();
            dto.simCard = simDto;
        }

         if (history.getAssignedBy() != null) {
            com.phone.dto.AssignmentHistoryDto.SimpleSystemUserDto sysUserDto = new com.phone.dto.AssignmentHistoryDto.SimpleSystemUserDto();
            sysUserDto.id = history.getAssignedBy().getId();
            sysUserDto.username = history.getAssignedBy().getUsername();
            dto.assignedBy = sysUserDto;
        }

        dto.acquisitionDate = history.getAcquisitionDate();
        dto.returnedDate = history.getReturnedDate();
        dto.status = history.getStatus() != null ? history.getStatus().name() : null;
        return dto;
    }

     /**
     * Converts a list of AssignmentHistory entities to a list of AssignmentHistoryDtos.
     * @param historyList The list of AssignmentHistory entities.
     * @return The corresponding list of AssignmentHistoryDtos.
     */
    public static java.util.List<com.phone.dto.AssignmentHistoryDto> toDtoList(java.util.List<AssignmentHistory> historyList) {
        java.util.List<com.phone.dto.AssignmentHistoryDto> dtoList = new java.util.ArrayList<>();
        for (AssignmentHistory history : historyList) {
            dtoList.add(toDto(history));
        }
        return dtoList;
    }
} 