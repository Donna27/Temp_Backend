package com.kbtg.tempbackend.interfaces.dtos;

public class LoginResponse {
    
    private String status;
    private String message;
    private String token;
    private String email;
    private String fullName;
    
    // Default constructor
    public LoginResponse() {}
    
    // Constructor for success response
    public LoginResponse(String status, String message, String token, String email, String fullName) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.email = email;
        this.fullName = fullName;
    }
    
    // Constructor for error response
    public LoginResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    
    // Getters and setters
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
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
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
}
