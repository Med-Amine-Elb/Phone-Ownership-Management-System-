package com.phone.dto;

/**
 * Data Transfer Object for updating SystemUser entities.
 * Used to receive user update requests with optional fields.
 */
public class UpdateSystemUserDto {
    /** The username of the system user. */
    public String username;
    
    /** The email of the system user. */
    public String email;
    
    /** The password of the system user (will be encoded if provided). */
    public String password;
    
    /** The role ID of the system user. */
    public Long roleId;
} 