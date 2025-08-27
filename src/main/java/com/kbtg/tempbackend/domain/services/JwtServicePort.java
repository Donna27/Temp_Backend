package com.kbtg.tempbackend.domain.services;

/**
 * JWT Service Port - Domain interface for JWT token operations
 * This interface defines the contract for JWT operations without framework dependencies
 */
public interface JwtServicePort {
    
    /**
     * Generate JWT token for user
     * @param email The user's email
     * @return The generated JWT token
     */
    String generateToken(String email);
    
    /**
     * Extract email from JWT token
     * @param token The JWT token
     * @return The email from token
     */
    String extractEmail(String token);
    
    /**
     * Validate JWT token
     * @param token The JWT token to validate
     * @param email The email to validate against
     * @return true if token is valid, false otherwise
     */
    boolean validateToken(String token, String email);
    
    /**
     * Check if token is expired
     * @param token The JWT token
     * @return true if token is expired, false otherwise
     */
    boolean isTokenExpired(String token);
}
