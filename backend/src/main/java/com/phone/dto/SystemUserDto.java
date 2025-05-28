package com.phone.dto;

/**
 * Data Transfer Object for SystemUser entities.
 * Used to expose system user data without sensitive information like password hash.
 */
public class SystemUserDto {
    /** The unique identifier of the system user. */
    public Long id;
    /** The username of the system user. */
    public String username;
    /** The role of the system user. */
    public String role;
} 