package com.phone.dto;

/**
 * Data Transfer Object for User entities.
 * Used to expose user data without sensitive information.
 */
public class UserDto {
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