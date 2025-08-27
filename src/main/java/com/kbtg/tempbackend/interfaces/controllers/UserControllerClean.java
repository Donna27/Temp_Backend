package com.kbtg.tempbackend.interfaces.controllers;

import com.kbtg.tempbackend.application.services.UserApplicationService;
import com.kbtg.tempbackend.domain.entities.UserEntity;
import com.kbtg.tempbackend.interfaces.dtos.UserRegistrationResponse;
import com.kbtg.tempbackend.interfaces.dtos.LoginRequest;
import com.kbtg.tempbackend.interfaces.dtos.LoginResponse;
import com.kbtg.tempbackend.interfaces.dtos.UserProfileResponse;
import com.kbtg.tempbackend.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller - Interface layer controller for user operations
 * Handles HTTP requests and delegates to Application Service
 */
@RestController
@RequestMapping("/api")
@Tag(name = "User Management", description = "APIs for user registration and management")
public class UserControllerClean {

    @Autowired
    private UserApplicationService userApplicationService;

    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Register a new user with email, password, firstname, lastname, phone number, and birthday"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User registered successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserRegistrationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserRegistrationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserRegistrationResponse.class)
            )
        )
    })
    public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody User user) {
        // Convert JPA entity to domain entity
        UserEntity userEntity = new UserEntity(
            user.getEmail(),
            user.getPassword(),
            user.getFirstname(),
            user.getLastname(),
            user.getPhoneNumber(),
            user.getBirthday()
        );
        
        UserRegistrationResponse response = userApplicationService.registerUser(userEntity);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticate user and return JWT token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponse.class)
            )
        )
    })
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userApplicationService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/me")
    @Operation(
        summary = "Get current user profile",
        description = "Retrieve the profile of the currently authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User profile retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserProfileResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserProfileResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserProfileResponse.class)
            )
        )
    })
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        try {
            // Get the current authenticated user from Security Context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserProfileResponse("error", "Unauthorized"));
            }
            
            String email = authentication.getName();
            UserProfileResponse response = userApplicationService.getUserProfile(email);
            if ("success".equals(response.getStatus())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new UserProfileResponse("error", "Failed to retrieve user profile: " + e.getMessage()));
        }
    }

    @GetMapping("/users/count")
    @Operation(
        summary = "Get total number of registered users",
        description = "Returns the total count of registered users"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Total user count retrieved successfully"
    )
    public ResponseEntity<Object> getTotalUsers() {
        int totalUserCount = userApplicationService.getTotalUsers();
        return ResponseEntity.ok(new Object() {
            public final String message = "Total users retrieved successfully";
            public final int totalUsers = totalUserCount;
        });
    }

    @GetMapping("/users/check-email/{email}")
    @Operation(
        summary = "Check if email exists",
        description = "Check if the provided email is already registered"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Email check completed"
    )
    public ResponseEntity<Object> checkEmailExists(@PathVariable String email) {
        boolean emailExists = userApplicationService.isEmailExists(email);
        return ResponseEntity.ok(new Object() {
            public final String message = "Email check completed";
            public final String emailAddress = email;
            public final boolean exists = emailExists;
        });
    }
}
