package com.kbtg.tempbackend.controller;

import com.kbtg.tempbackend.model.User;
import com.kbtg.tempbackend.dto.UserRegistrationResponse;
import com.kbtg.tempbackend.dto.LoginRequest;
import com.kbtg.tempbackend.dto.LoginResponse;
import com.kbtg.tempbackend.dto.UserProfileResponse;
import com.kbtg.tempbackend.service.UserService;
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

@RestController
@RequestMapping("/api")
@Tag(name = "User Management", description = "APIs for user registration and management")
public class UserController {

    @Autowired
    private UserService userService;

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
        UserRegistrationResponse response = userService.registerUser(user);
        
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if ("Email already exists".equals(response.getMessage())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticate user with email and password, returns JWT token on success"
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
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponse.class)
            )
        )
    })
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
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
        int totalUsers = userService.getTotalUsers();
        return ResponseEntity.ok(new Object() {
            public final String message = "Total users retrieved successfully";
            public final int totalUsers = userService.getTotalUsers();
        });
    }

    @GetMapping("/users/check-email/{email}")
    @Operation(
        summary = "Check if email exists",
        description = "Check if the provided email is already registered"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Email check completed successfully"
    )
    public ResponseEntity<Object> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.isEmailExists(email);
        return ResponseEntity.ok(new Object() {
            public final String emailAddress = email;
            public final boolean exists = userService.isEmailExists(email);
            public final String message = exists ? "Email already exists" : "Email is available";
        });
    }

    @GetMapping("/me")
    @Operation(
        summary = "Get current user profile",
        description = "Get the profile information of the currently authenticated user from JWT token"
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
            description = "Unauthorized - Invalid or missing JWT token",
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
            UserProfileResponse response = userService.getUserProfile(email);
            
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
}
