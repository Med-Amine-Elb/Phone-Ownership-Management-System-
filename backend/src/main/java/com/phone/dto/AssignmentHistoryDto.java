package com.phone.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for AssignmentHistory entities.
 * Used to expose assignment history data.
 */
public class AssignmentHistoryDto {
    /** The unique identifier of the assignment history entry. */
    public Long id;
    /** The phone assignment associated with this history entry. */
    public Long phoneAssignmentId; // Or a simplified PhoneAssignment DTO if needed
    /** The user who was assigned the phone. */
    public SimpleUserDto user;
    /** The phone that was assigned. */
    public SimplePhoneDto phone;
    /** The SIM card that was assigned. */
    public SimpleSimCardDto simCard;
     /** The system user who performed the assignment. */
    public SimpleSystemUserDto assignedBy;
    /** The acquisition date of the assignment. */
    public LocalDateTime acquisitionDate;
    /** The returned date of the assignment. */
    public LocalDateTime returnedDate;
    /** The status of the history entry. */
    public String status;

     /**
     * Simple Data Transfer Object for User entities within AssignmentHistoryDto.
     */
    public static class SimpleUserDto {
        /** The unique identifier of the user. */
        public Long id;
        /** The first name of the user. */
        public String firstName;
        /** The last name of the user. */
        public String lastName;
    }
    /**
     * Simple Data Transfer Object for Phone entities within AssignmentHistoryDto.
     */
    public static class SimplePhoneDto {
        /** The unique identifier of the phone. */
        public Long id;
        /** The brand of the phone. */
        public String brand;
        /** The model of the phone. */
        public String model;
    }
    /**
     * Simple Data Transfer Object for SimCard entities within AssignmentHistoryDto.
     */
    public static class SimpleSimCardDto {
        /** The unique identifier of the SIM card. */
        public Long id;
        /** The number of the SIM card. */
        public String number;
    }
      /**
     * Simple Data Transfer Object for SystemUser entities within AssignmentHistoryDto.
     */
    public static class SimpleSystemUserDto {
        /** The unique identifier of the system user. */
        public Long id;
        /** The username of the system user. */
        public String username;
    }
} 