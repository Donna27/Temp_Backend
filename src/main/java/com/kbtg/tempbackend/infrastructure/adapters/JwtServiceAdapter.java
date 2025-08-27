package com.kbtg.tempbackend.infrastructure.adapters;

import com.kbtg.tempbackend.domain.services.JwtServicePort;
import com.kbtg.tempbackend.util.JwtUtil;
import org.springframework.stereotype.Component;

/**
 * JWT Service Adapter - Infrastructure adapter implementing domain JWT service port
 * Bridges between domain layer and JWT utility implementation
 */
@Component
public class JwtServiceAdapter implements JwtServicePort {
    
    private final JwtUtil jwtUtil;
    
    public JwtServiceAdapter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public String generateToken(String email) {
        return jwtUtil.generateToken(email);
    }
    
    @Override
    public String extractEmail(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }
    
    @Override
    public boolean validateToken(String token, String email) {
        return jwtUtil.validateToken(token, email);
    }
    
    @Override
    public boolean isTokenExpired(String token) {
        return jwtUtil.isTokenExpired(token);
    }
}
