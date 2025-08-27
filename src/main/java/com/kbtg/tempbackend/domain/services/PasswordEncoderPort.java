package com.kbtg.tempbackend.domain.services;

/**
 * Password Encoder Port - Domain interface for password encoding operations
 * This interface defines the contract for password encoding without framework dependencies
 */
public interface PasswordEncoderPort {
    
    /**
     * Encode a raw password
     * @param rawPassword The raw password to encode
     * @return The encoded password
     */
    String encode(String rawPassword);
    
    /**
     * Check if raw password matches encoded password
     * @param rawPassword The raw password
     * @param encodedPassword The encoded password
     * @return true if passwords match, false otherwise
     */
    boolean matches(String rawPassword, String encodedPassword);
}
