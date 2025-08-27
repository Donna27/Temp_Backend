package com.kbtg.tempbackend.application.services;

import com.kbtg.tempbackend.application.usecases.GetUserProfileUseCase;
import com.kbtg.tempbackend.application.usecases.LoginUserUseCase;
import com.kbtg.tempbackend.application.usecases.RegisterUserUseCase;
import com.kbtg.tempbackend.domain.entities.UserEntity;
import com.kbtg.tempbackend.domain.repositories.UserRepositoryPort;
import com.kbtg.tempbackend.domain.services.JwtServicePort;
import com.kbtg.tempbackend.domain.services.PasswordEncoderPort;
import com.kbtg.tempbackend.interfaces.dtos.LoginResponse;
import com.kbtg.tempbackend.interfaces.dtos.UserProfileResponse;
import com.kbtg.tempbackend.interfaces.dtos.UserRegistrationResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User Application Service - Application layer service orchestrating use cases
 * Acts as a facade for the use cases and handles DTO conversions
 */
@Service
public class UserApplicationService {
    
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UserRepositoryPort userRepository;
    
    public UserApplicationService(UserRepositoryPort userRepository,
                                PasswordEncoderPort passwordEncoder,
                                JwtServicePort jwtService) {
        this.userRepository = userRepository;
        this.registerUserUseCase = new RegisterUserUseCase(userRepository, passwordEncoder);
        this.loginUserUseCase = new LoginUserUseCase(userRepository, passwordEncoder, jwtService);
        this.getUserProfileUseCase = new GetUserProfileUseCase(userRepository);
    }
    
    /**
     * Register a new user
     * @param user The user entity to register
     * @return UserRegistrationResponse DTO
     */
    public UserRegistrationResponse registerUser(UserEntity user) {
        try {
            UserEntity savedUser = registerUserUseCase.execute(user);
            
            // Convert to response DTO
            String fullName = savedUser.getFullName();
            return new UserRegistrationResponse(
                "success",
                "User registered successfully",
                savedUser.getId(),
                savedUser.getEmail(),
                fullName,
                savedUser.getPhoneNumber(),
                savedUser.getBirthday()
            );
            
        } catch (IllegalArgumentException e) {
            return new UserRegistrationResponse("error", e.getMessage());
        } catch (Exception e) {
            return new UserRegistrationResponse("error", "Failed to register user: " + e.getMessage());
        }
    }
    
    /**
     * Authenticate user and generate JWT token
     * @param email User's email
     * @param password User's password
     * @return LoginResponse DTO
     */
    public LoginResponse loginUser(String email, String password) {
        try {
            LoginUserUseCase.LoginResult result = loginUserUseCase.execute(email, password);
            
            if (result.isSuccess()) {
                return new LoginResponse("success", result.getMessage(), result.getToken(), email, null);
            } else {
                return new LoginResponse("error", result.getMessage());
            }
            
        } catch (Exception e) {
            return new LoginResponse("error", "Login failed: " + e.getMessage());
        }
    }
    
    /**
     * Get user profile by email
     * @param email User's email
     * @return UserProfileResponse DTO
     */
    public UserProfileResponse getUserProfile(String email) {
        try {
            Optional<UserEntity> userOpt = getUserProfileUseCase.execute(email);
            
            if (userOpt.isEmpty()) {
                return new UserProfileResponse("error", "User not found");
            }
            
            UserEntity user = userOpt.get();
            return new UserProfileResponse(
                "success",
                "User profile retrieved successfully",
                user.getId(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhoneNumber(),
                user.getBirthday()
            );
            
        } catch (Exception e) {
            return new UserProfileResponse("error", "Failed to retrieve user profile: " + e.getMessage());
        }
    }
    
    /**
     * Check if email exists
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Get user by email
     * @param email User's email
     * @return UserEntity or null if not found
     */
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
    /**
     * Get total number of users
     * @return Total user count
     */
    public int getTotalUsers() {
        return (int) userRepository.count();
    }
}
