package com.kbtg.tempbackend.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Schema(description = "User registration response")
public class UserRegistrationResponse {
    
    @Schema(description = "Registration status", example = "success")
    private String status;
    
    @Schema(description = "Response message", example = "User registered successfully")
    private String message;
    
    @Schema(description = "User ID", example = "1")
    private Long userId;
    
    @Schema(description = "User's email", example = "user@example.com")
    private String email;
    
    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;
    
    @Schema(description = "User's phone number", example = "0812345678")
    private String phoneNumber;
    
    @Schema(description = "User's birthday", example = "1990-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    // Default constructor
    public UserRegistrationResponse() {}

    // Constructor for success response
    public UserRegistrationResponse(String status, String message, Long userId, 
                                  String email, String fullName, String phoneNumber, 
                                  LocalDate birthday) {
        this.status = status;
        this.message = message;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    // Constructor for error response
    public UserRegistrationResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
