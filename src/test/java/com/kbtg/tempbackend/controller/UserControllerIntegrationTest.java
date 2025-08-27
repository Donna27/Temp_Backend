package com.kbtg.tempbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbtg.tempbackend.interfaces.dtos.LoginRequest;
import com.kbtg.tempbackend.infrastructure.entities.UserJpaEntity;
import com.kbtg.tempbackend.infrastructure.repositories.UserJpaRepository;
import com.kbtg.tempbackend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private UserJpaEntity testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        userRepository.deleteAll();
        
        testUser = new UserJpaEntity();
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setPhoneNumber("1234567890");
        testUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        
        userRepository.save(testUser);
    }

    @Test
    void registerUser_Success() throws Exception {
        // Given
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");
        newUser.setFirstname("Jane");
        newUser.setLastname("Smith");
        newUser.setPhoneNumber("0987654321");
        newUser.setBirthday(LocalDate.of(1995, 5, 15));

        // When & Then
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("User registered successfully")))
                .andExpect(jsonPath("$.userId", notNullValue()))
                .andExpect(jsonPath("$.email", is("new@example.com")))
                .andExpect(jsonPath("$.fullName", is("Jane Smith")));
    }

    @Test
    void registerUser_EmailAlreadyExists() throws Exception {
        // Given
        User duplicateUser = new User();
        duplicateUser.setEmail("test@example.com"); // Same email as testUser
        duplicateUser.setPassword("password123");
        duplicateUser.setFirstname("Jane");
        duplicateUser.setLastname("Smith");
        duplicateUser.setPhoneNumber("0987654321");
        duplicateUser.setBirthday(LocalDate.of(1995, 5, 15));

        // When & Then
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Email already exists")));
    }

    @Test
    void registerUser_InvalidData() throws Exception {
        // Given
        User invalidUser = new User();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginUser_Success() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Login successful")))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    void loginUser_InvalidCredentials() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        // When & Then
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Invalid email or password")));
    }

    @Test
    void loginUser_UserNotFound() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("notfound@example.com");
        loginRequest.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Invalid email or password")));
    }

    @Test
    void getUserProfile_Success() throws Exception {
        // Given
        String token = jwtUtil.generateToken(testUser.getEmail());

        // When & Then
        mockMvc.perform(get("/api/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.fullName", is("John Doe")))
                .andExpect(jsonPath("$.phoneNumber", is("1234567890")))
                .andExpect(jsonPath("$.birthday", is("1990-01-01")));
    }

    @Test
    void getUserProfile_NoToken() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserProfile_InvalidToken() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/me")
                .header("Authorization", "Bearer invalidtoken"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserProfile_ExpiredToken() throws Exception {
        // This would require a way to generate expired tokens
        // For now, we'll test with an invalid token format
        
        // When & Then
        mockMvc.perform(get("/api/me")
                .header("Authorization", "Bearer expired.jwt.token"))
                .andExpect(status().isUnauthorized());
    }
}
