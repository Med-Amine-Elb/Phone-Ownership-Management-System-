package com.phone.dto;

/**
 * Data Transfer Object for SimCard entities.
 * Used to expose SIM card data without sensitive information like PIN/PUK.
 */
public class SimCardDto {
    /** The unique identifier of the SIM card. */
    public Long id;
    /** The phone number associated with the SIM card. */
    public String number;
    /** The operator of the SIM card. */
    public String operator;
    /** The forfait plan of the SIM card. */
    public String forfait;
    /** The assignment status of the SIM card. */
    public boolean assigned;
} 