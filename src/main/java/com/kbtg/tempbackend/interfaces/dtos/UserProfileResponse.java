package com.kbtg.tempbackend.interfaces.dtos;

import java.time.LocalDate;

public class UserProfileResponse {
    private String status;
    private String message;
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthday;

    public UserProfileResponse() {}

    public UserProfileResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public UserProfileResponse(String status, String message, Long id, String email, 
                             String firstname, String lastname, String phoneNumber, LocalDate birthday) {
        this.status = status;
        this.message = message;
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
