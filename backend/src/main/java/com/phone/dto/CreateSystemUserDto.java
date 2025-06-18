package com.phone.dto;

/**
 * Data Transfer Object for creating SystemUser entities.
 * Used to receive user creation requests with proper validation.
 */
public class CreateSystemUserDto {
    /** The username of the system user. */
    public String username;
    
    /** The email of the system user. */
    public String email;
    
    /** The password of the system user (will be encoded). */
    public String password;
    
    /** The role ID of the system user. */
    public Long roleId;
} 