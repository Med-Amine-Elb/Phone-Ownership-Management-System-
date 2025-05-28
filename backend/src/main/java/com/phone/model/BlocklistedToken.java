package com.phone.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "blocklisted_tokens")
public class BlocklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500) // Adjust length based on your token size
    private String token;

    @Column(nullable = false)
    private Instant expiryDate; // Store the original token expiry for cleanup

    // Getters and setters (or use Lombok @Data)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
} 