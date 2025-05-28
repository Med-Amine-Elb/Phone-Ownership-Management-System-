package com.phone.dto;

/**
 * Data Transfer Object for Phone entities.
 * Used to expose phone data without sensitive information.
 */
public class PhoneDto {
    /** The unique identifier of the phone. */
    public Long id;
    /** The brand of the phone. */
    public String brand;
    /** The model of the phone. */
    public String model;
    /** The active status of the phone. */
    public boolean active;
} 