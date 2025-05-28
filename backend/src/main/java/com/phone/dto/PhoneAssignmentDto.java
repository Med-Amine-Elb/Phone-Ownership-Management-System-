package com.phone.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for PhoneAssignment entities.
 * Used to expose phone assignment data without sensitive information.
 */
public class PhoneAssignmentDto {
    /** The unique identifier of the phone assignment. */
    public Long id;
    /** The user assigned to the phone. */
    public SimpleUserDto user;
    /** The phone assigned. */
    public SimplePhoneDto phone;
    /** The SIM card assigned. */
    public SimpleSimCardDto simCard;
    /** The date the phone was acquired. */
    public LocalDateTime acquisitionDate;
    /** The date the phone is next eligible for upgrade. */
    public LocalDateTime nextUpgradeDate;
    /** The URL of the PV file. */
    public String pvFileUrl;
    /** The system user who assigned the phone. */
    public SimpleSystemUserDto assignedBy;
    /** The status of the phone assignment. */
    public String status;
    /** The date the phone was returned. */
    public LocalDateTime returnedDate;

    /**
     * Simple Data Transfer Object for User entities within PhoneAssignmentDto.
     */
    public static class SimpleUserDto {
        /** The unique identifier of the user. */
        public Long id;
        /** The first name of the user. */
        public String firstName;
        /** The last name of the user. */
        public String lastName;
        /** The department of the user. */
        public String department;
        /** The status of the user. */
        public String status;
    }
    /**
     * Simple Data Transfer Object for Phone entities within PhoneAssignmentDto.
     */
    public static class SimplePhoneDto {
        /** The unique identifier of the phone. */
        public Long id;
        /** The brand of the phone. */
        public String brand;
        /** The model of the phone. */
        public String model;
        /** The active status of the phone. */
        public boolean active;
    }
    /**
     * Simple Data Transfer Object for SimCard entities within PhoneAssignmentDto.
     */
    public static class SimpleSimCardDto {
        /** The unique identifier of the SIM card. */
        public Long id;
        /** The number of the SIM card. */
        public String number;
        /** The operator of the SIM card. */
        public String operator;
        /** The forfait of the SIM card. */
        public String forfait;
        /** The assigned status of the SIM card. */
        public boolean assigned;
    }
    /**
     * Simple Data Transfer Object for SystemUser entities within PhoneAssignmentDto.
     */
    public static class SimpleSystemUserDto {
        /** The unique identifier of the system user. */
        public Long id;
        /** The username of the system user. */
        public String username;
        /** The role of the system user. */
        public String role;
    }
} 