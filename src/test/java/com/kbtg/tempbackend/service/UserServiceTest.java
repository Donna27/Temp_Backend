package com.kbtg.tempbackend.service;

import com.kbtg.tempbackend.dto.LoginRequest;
import com.kbtg.tempbackend.dto.LoginResponse;
import com.kbtg.tempbackend.dto.UserProfileResponse;
import com.kbtg.tempbackend.dto.UserRegistrationResponse;
import com.kbtg.tempbackend.model.User;
import com.kbtg.tempbackend.repository.UserRepository;
import com.kbtg.tempbackend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setPhoneNumber("1234567890");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
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

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserRegistrationResponse response = userService.registerUser(newUser);

        // Then
        assertEquals("success", response.getStatus());
        assertEquals("User registered successfully", response.getMessage());
        assertNotNull(response.getUserId());
        assertEquals(testUser.getEmail(), response.getEmail());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When
        UserRegistrationResponse response = userService.registerUser(testUser);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("Email already exists", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUser_Success() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");

        // When
        LoginResponse response = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

        // Then
        assertEquals("success", response.getStatus());
        assertEquals("Login successful", response.getMessage());
        assertEquals("jwt-token", response.getToken());
        assertEquals(testUser.getEmail(), response.getEmail());
    }

    @Test
    void loginUser_UserNotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When
        LoginResponse response = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("Invalid email or password", response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    void loginUser_InvalidPassword() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When
        LoginResponse response = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("Invalid email or password", response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    void getUserProfile_Success() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        // When
        UserProfileResponse response = userService.getUserProfile("test@example.com");

        // Then
        assertNotNull(response);
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getFirstname(), response.getFirstname());
        assertEquals(testUser.getLastname(), response.getLastname());
        assertEquals(testUser.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(testUser.getBirthday(), response.getBirthday());
    }

    @Test
    void getUserProfile_UserNotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When
        UserProfileResponse response = userService.getUserProfile("notfound@example.com");

        // Then
        assertNull(response);
    }

    @Test
    void isEmailExists_True() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When
        boolean exists = userService.isEmailExists("test@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void isEmailExists_False() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // When
        boolean exists = userService.isEmailExists("test@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    void getUserByEmail_Found() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        // When
        User user = userService.getUserByEmail("test@example.com");

        // Then
        assertNotNull(user);
        assertEquals(testUser.getEmail(), user.getEmail());
    }

    @Test
    void getUserByEmail_NotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When
        User user = userService.getUserByEmail("notfound@example.com");

        // Then
        assertNull(user);
    }

    @Test
    void getTotalUsers() {
        // Given
        when(userRepository.count()).thenReturn(5L);

        // When
        int totalUsers = userService.getTotalUsers();

        // Then
        assertEquals(5, totalUsers);
    }
}
