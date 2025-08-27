package com.kbtg.tempbackend.infrastructure.adapters;

import com.kbtg.tempbackend.domain.services.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Password Encoder Adapter - Infrastructure adapter implementing domain password encoder port
 * Bridges between domain layer and Spring Security BCrypt encoder
 */
@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {
    
    private final PasswordEncoder springPasswordEncoder;
    
    public PasswordEncoderAdapter(PasswordEncoder springPasswordEncoder) {
        this.springPasswordEncoder = springPasswordEncoder;
    }
    
    @Override
    public String encode(String rawPassword) {
        return springPasswordEncoder.encode(rawPassword);
    }
    
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return springPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
