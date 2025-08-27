package com.kbtg.tempbackend.service;

import com.kbtg.tempbackend.interfaces.dtos.LoginRequest;
import com.kbtg.tempbackend.interfaces.dtos.LoginResponse;
import com.kbtg.tempbackend.interfaces.dtos.UserProfileResponse;
import com.kbtg.tempbackend.interfaces.dtos.UserRegistrationResponse;
import com.kbtg.tempbackend.application.services.UserApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserApplicationService userApplicationService;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("test@example.com", "password123");
    }

    @Test
    void loginUser_Success() {
        // Given
        LoginResponse expectedResponse = new LoginResponse("token123", "Login successful");
        when(userApplicationService.loginUser(any(LoginRequest.class))).thenReturn(expectedResponse);

        // When
        LoginResponse response = userApplicationService.loginUser(loginRequest);

        // Then
        assertEquals("token123", response.token());
        assertEquals("Login successful", response.message());
        verify(userApplicationService).loginUser(loginRequest);
    }

    @Test
    void loginUser_InvalidCredentials() {
        // Given
        LoginResponse expectedResponse = new LoginResponse(null, "Invalid credentials");
        when(userApplicationService.loginUser(any(LoginRequest.class))).thenReturn(expectedResponse);

        // When
        LoginResponse response = userApplicationService.loginUser(loginRequest);

        // Then
        assertNull(response.token());
        assertEquals("Invalid credentials", response.message());
        verify(userApplicationService).loginUser(loginRequest);
    }

    @Test
    void getUserProfile_Success() {
        // Given
        UserProfileResponse expectedResponse = new UserProfileResponse(
                1L, "test@example.com", "John", "Doe", "123456789", LocalDate.of(1990, 1, 1)
        );
        when(userApplicationService.getUserProfile(anyString())).thenReturn(Optional.of(expectedResponse));

        // When
        Optional<UserProfileResponse> result = userApplicationService.getUserProfile("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id());
        assertEquals("test@example.com", result.get().email());
        assertEquals("John", result.get().firstname());
        verify(userApplicationService).getUserProfile("test@example.com");
    }

    @Test
    void getUserProfile_UserNotFound() {
        // Given
        when(userApplicationService.getUserProfile(anyString())).thenReturn(Optional.empty());

        // When
        Optional<UserProfileResponse> result = userApplicationService.getUserProfile("nonexistent@example.com");

        // Then
        assertFalse(result.isPresent());
        verify(userApplicationService).getUserProfile("nonexistent@example.com");
    }
}
