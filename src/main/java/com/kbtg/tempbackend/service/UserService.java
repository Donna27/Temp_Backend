package com.kbtg.tempbackend.service;

import com.kbtg.tempbackend.model.User;
import com.kbtg.tempbackend.dto.UserRegistrationResponse;
import com.kbtg.tempbackend.dto.LoginResponse;
import com.kbtg.tempbackend.dto.UserProfileResponse;
import com.kbtg.tempbackend.repository.UserRepository;
import com.kbtg.tempbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public UserRegistrationResponse registerUser(User user) {
        try {
            // Check if email already exists
            if (userRepository.existsByEmail(user.getEmail())) {
                return new UserRegistrationResponse("error", "Email already exists");
            }
            
            // Encode password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // Save user to database
            User savedUser = userRepository.save(user);
            
            // Create success response
            String fullName = savedUser.getFirstname() + " " + savedUser.getLastname();
            
            return new UserRegistrationResponse(
                "success",
                "User registered successfully",
                savedUser.getId(),
                savedUser.getEmail(),
                fullName,
                savedUser.getPhoneNumber(),
                savedUser.getBirthday()
            );
            
        } catch (Exception e) {
            return new UserRegistrationResponse("error", "Registration failed: " + e.getMessage());
        }
    }
    
    public LoginResponse loginUser(String email, String password) {
        try {
            // Find user by email
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return new LoginResponse("error", "Invalid email or password");
            }
            
            // Check password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return new LoginResponse("error", "Invalid email or password");
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());
            String fullName = user.getFirstname() + " " + user.getLastname();
            
            return new LoginResponse("success", "Login successful", token, user.getEmail(), fullName);
            
        } catch (Exception e) {
            return new LoginResponse("error", "Login failed: " + e.getMessage());
        }
    }
    
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
    public int getTotalUsers() {
        return (int) userRepository.count();
    }
    
    public UserProfileResponse getUserProfile(String email) {
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return new UserProfileResponse("error", "User not found");
            }
            
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
}
