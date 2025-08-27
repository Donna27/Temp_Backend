package com.kbtg.tempbackend.application.usecases;

import com.kbtg.tempbackend.domain.entities.UserEntity;
import com.kbtg.tempbackend.domain.repositories.UserRepositoryPort;
import com.kbtg.tempbackend.domain.services.PasswordEncoderPort;

/**
 * Register User Use Case - Application layer business logic for user registration
 * Orchestrates the registration process following Clean Architecture principles
 */
public class RegisterUserUseCase {
    
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    
    public RegisterUserUseCase(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Execute user registration
     * @param user The user entity to register
     * @return The registered user entity
     * @throws IllegalArgumentException if email already exists
     */
    public UserEntity execute(UserEntity user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Encode password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // Save user
        return userRepository.save(user);
    }
}
