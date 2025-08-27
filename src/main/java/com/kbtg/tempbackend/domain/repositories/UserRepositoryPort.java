package com.kbtg.tempbackend.domain.repositories;

import com.kbtg.tempbackend.domain.entities.UserEntity;
import java.util.List;
import java.util.Optional;

/**
 * User Repository Port - Domain interface for user data access
 * This interface defines the contract for user data operations
 * without any framework dependencies
 */
public interface UserRepositoryPort {
    
    /**
     * Save a user entity
     * @param user The user to save
     * @return The saved user with generated ID
     */
    UserEntity save(UserEntity user);
    
    /**
     * Find user by email
     * @param email The user's email
     * @return Optional containing the user if found
     */
    Optional<UserEntity> findByEmail(String email);
    
    /**
     * Find user by ID
     * @param id The user's ID
     * @return Optional containing the user if found
     */
    Optional<UserEntity> findById(Long id);
    
    /**
     * Check if email exists
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Get all users
     * @return List of all users
     */
    List<UserEntity> findAll();
    
    /**
     * Count total number of users
     * @return Total count of users
     */
    long count();
    
    /**
     * Delete user by ID
     * @param id The user's ID
     */
    void deleteById(Long id);
    
    /**
     * Delete all users (mainly for testing)
     */
    void deleteAll();
}
