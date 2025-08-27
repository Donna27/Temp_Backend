package com.kbtg.tempbackend.application;

import com.kbtg.tempbackend.application.services.UserApplicationService;
import com.kbtg.tempbackend.application.usecases.GetUserProfileUseCase;
import com.kbtg.tempbackend.application.usecases.LoginUserUseCase;
import com.kbtg.tempbackend.application.usecases.RegisterUserUseCase;
import com.kbtg.tempbackend.domain.entities.UserEntity;
import com.kbtg.tempbackend.interfaces.dtos.LoginRequest;
import com.kbtg.tempbackend.interfaces.dtos.LoginResponse;
import com.kbtg.tempbackend.interfaces.dtos.UserProfileResponse;
import com.kbtg.tempbackend.interfaces.dtos.UserRegistrationRequest;
import com.kbtg.tempbackend.interfaces.dtos.UserRegistrationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private LoginUserUseCase loginUserUseCase;

    @Mock
    private GetUserProfileUseCase getUserProfileUseCase;

    @InjectMocks
    private UserApplicationService userApplicationService;

    private UserEntity testUser;
    private LoginRequest loginRequest;
    private UserRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity.Builder()
                .withId(1L)
                .withEmail("test@example.com")
                .withPassword("encodedPassword")
                .withFirstname("John")
                .withLastname("Doe")
                .withPhoneNumber("123456789")
                .withDateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        loginRequest = new LoginRequest("test@example.com", "password");
        
        registrationRequest = new UserRegistrationRequest(
                "test@example.com", 
                "password", 
                "John", 
                "Doe", 
                "123456789", 
                LocalDate.of(1990, 1, 1)
        );
    }

    @Test
    void registerUser_Success() {
        // Given
        UserRegistrationResponse expectedResponse = new UserRegistrationResponse("Registration successful");
        when(registerUserUseCase.execute(any(UserRegistrationRequest.class))).thenReturn(expectedResponse);

        // When
        UserRegistrationResponse result = userApplicationService.registerUser(registrationRequest);

        // Then
        assertNotNull(result);
        assertEquals("Registration successful", result.message());
        verify(registerUserUseCase, times(1)).execute(registrationRequest);
    }

    @Test
    void registerUser_UserAlreadyExists() {
        // Given
        UserRegistrationResponse expectedResponse = new UserRegistrationResponse("User already exists");
        when(registerUserUseCase.execute(any(UserRegistrationRequest.class))).thenReturn(expectedResponse);

        // When
        UserRegistrationResponse result = userApplicationService.registerUser(registrationRequest);

        // Then
        assertNotNull(result);
        assertEquals("User already exists", result.message());
        verify(registerUserUseCase, times(1)).execute(registrationRequest);
    }

    @Test
    void loginUser_Success() {
        // Given
        LoginResponse expectedResponse = new LoginResponse("token123", "Login successful");
        when(loginUserUseCase.execute(any(LoginRequest.class))).thenReturn(expectedResponse);

        // When
        LoginResponse result = userApplicationService.loginUser(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals("token123", result.token());
        assertEquals("Login successful", result.message());
        verify(loginUserUseCase, times(1)).execute(loginRequest);
    }

    @Test
    void loginUser_InvalidCredentials() {
        // Given
        LoginResponse expectedResponse = new LoginResponse(null, "Invalid credentials");
        when(loginUserUseCase.execute(any(LoginRequest.class))).thenReturn(expectedResponse);

        // When
        LoginResponse result = userApplicationService.loginUser(loginRequest);

        // Then
        assertNotNull(result);
        assertNull(result.token());
        assertEquals("Invalid credentials", result.message());
        verify(loginUserUseCase, times(1)).execute(loginRequest);
    }

    @Test
    void getUserProfile_Success() {
        // Given
        UserProfileResponse expectedResponse = new UserProfileResponse(
                1L, "test@example.com", "John", "Doe", "123456789", LocalDate.of(1990, 1, 1)
        );
        when(getUserProfileUseCase.execute(anyString())).thenReturn(Optional.of(expectedResponse));

        // When
        Optional<UserProfileResponse> result = userApplicationService.getUserProfile("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id());
        assertEquals("test@example.com", result.get().email());
        assertEquals("John", result.get().firstname());
        verify(getUserProfileUseCase, times(1)).execute("test@example.com");
    }

    @Test
    void getUserProfile_UserNotFound() {
        // Given
        when(getUserProfileUseCase.execute(anyString())).thenReturn(Optional.empty());

        // When
        Optional<UserProfileResponse> result = userApplicationService.getUserProfile("nonexistent@example.com");

        // Then
        assertFalse(result.isPresent());
        verify(getUserProfileUseCase, times(1)).execute("nonexistent@example.com");
    }
}
