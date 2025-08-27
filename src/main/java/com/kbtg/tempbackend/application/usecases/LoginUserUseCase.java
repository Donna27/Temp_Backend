package com.kbtg.tempbackend.application.usecases;

import com.kbtg.tempbackend.domain.entities.UserEntity;
import com.kbtg.tempbackend.domain.repositories.UserRepositoryPort;
import com.kbtg.tempbackend.domain.services.PasswordEncoderPort;
import com.kbtg.tempbackend.domain.services.JwtServicePort;

import java.util.Optional;

/**
 * Login User Use Case - Application layer business logic for user authentication
 * Orchestrates the login process following Clean Architecture principles
 */
public class LoginUserUseCase {
    
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtServicePort jwtService;
    
    public LoginUserUseCase(UserRepositoryPort userRepository, 
                           PasswordEncoderPort passwordEncoder, 
                           JwtServicePort jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    
    /**
     * Execute user login
     * @param email The user's email
     * @param password The user's password
     * @return LoginResult containing success status and token if successful
     */
    public LoginResult execute(String email, String password) {
        // Find user by email
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return new LoginResult(false, null, "User not found");
        }
        
        UserEntity user = userOpt.get();
        
        // Check password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new LoginResult(false, null, "Invalid credentials");
        }
        
        // Generate token
        String token = jwtService.generateToken(user.getEmail());
        
        return new LoginResult(true, token, "Login successful");
    }
    
    /**
     * Login Result DTO
     */
    public static class LoginResult {
        private final boolean success;
        private final String token;
        private final String message;
        
        public LoginResult(boolean success, String token, String message) {
            this.success = success;
            this.token = token;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getToken() {
            return token;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
