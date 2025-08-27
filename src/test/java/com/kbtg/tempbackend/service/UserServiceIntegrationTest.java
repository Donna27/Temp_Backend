package com.kbtg.tempbackend.service;

import com.kbtg.tempbackend.dto.LoginResponse;
import com.kbtg.tempbackend.dto.UserProfileResponse;
import com.kbtg.tempbackend.dto.UserRegistrationResponse;
import com.kbtg.tempbackend.model.User;
import com.kbtg.tempbackend.repository.UserRepository;
import com.kbtg.tempbackend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setPhoneNumber("1234567890");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
        
        userRepository.save(testUser);
    }

    @Test
    void registerUser_Success() {
        // Given
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");
        newUser.setFirstname("Jane");
        newUser.setLastname("Smith");
        newUser.setPhoneNumber("0987654321");
        newUser.setBirthday(LocalDate.of(1995, 5, 15));

        // When
        UserRegistrationResponse response = userService.registerUser(newUser);

        // Then
        assertEquals("success", response.getStatus());
        assertEquals("User registered successfully", response.getMessage());
        assertNotNull(response.getUserId());
        assertEquals("new@example.com", response.getEmail());
    }

    @Test
    void loginUser_Success() {
        // When
        LoginResponse response = userService.loginUser("test@example.com", "password123");

        // Then
        assertEquals("success", response.getStatus());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getToken());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void getUserProfile_Success() {
        // When
        UserProfileResponse response = userService.getUserProfile("test@example.com");

        // Then
        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getFirstname());
        assertEquals("Doe", response.getLastname());
    }
}
