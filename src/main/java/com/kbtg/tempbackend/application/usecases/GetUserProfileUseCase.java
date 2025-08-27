package com.kbtg.tempbackend.application.usecases;

import com.kbtg.tempbackend.domain.entities.UserEntity;
import com.kbtg.tempbackend.domain.repositories.UserRepositoryPort;

import java.util.Optional;

/**
 * Get User Profile Use Case - Application layer business logic for retrieving user profile
 * Orchestrates the profile retrieval process following Clean Architecture principles
 */
public class GetUserProfileUseCase {
    
    private final UserRepositoryPort userRepository;
    
    public GetUserProfileUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Execute get user profile by email
     * @param email The user's email
     * @return Optional containing the user entity if found
     */
    public Optional<UserEntity> execute(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Execute get user profile by ID
     * @param id The user's ID
     * @return Optional containing the user entity if found
     */
    public Optional<UserEntity> execute(Long id) {
        return userRepository.findById(id);
    }
}
