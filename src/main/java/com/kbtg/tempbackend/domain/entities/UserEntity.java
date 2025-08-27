package com.kbtg.tempbackend.domain.entities;

import java.time.LocalDate;

/**
 * User Domain Entity - Core business entity without framework dependencies
 * Represents a user in the system with all business rules and validations
 */
public class UserEntity {
    
    private Long id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthday;

    // Default constructor
    public UserEntity() {}

    // Constructor with all fields except id (for new users)
    public UserEntity(String email, String password, String firstname, String lastname, 
                     String phoneNumber, LocalDate birthday) {
        validateEmail(email);
        validatePassword(password);
        validateName(firstname, "First name");
        validateName(lastname, "Last name");
        validatePhoneNumber(phoneNumber);
        validateBirthday(birthday);
        
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    // Constructor with id (for existing users)
    public UserEntity(Long id, String email, String password, String firstname, String lastname, 
                     String phoneNumber, LocalDate birthday) {
        this(email, password, firstname, lastname, phoneNumber, birthday);
        this.id = id;
    }

    // Business logic methods
    public String getFullName() {
        return firstname + " " + lastname;
    }

    public boolean isEmailValid() {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean isAdult() {
        if (birthday == null) return false;
        return LocalDate.now().minusYears(18).isAfter(birthday);
    }

    // Validation methods
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email should be valid");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    private void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (!phoneNumber.matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
    }

    private void validateBirthday(LocalDate birthday) {
        if (birthday == null) {
            throw new IllegalArgumentException("Birthday is required");
        }
        if (!birthday.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Birthday must be in the past");
        }
    }

    // Getters and Setters
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
        validateEmail(email);
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        validateName(firstname, "First name");
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        validateName(lastname, "Last name");
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        validateBirthday(birthday);
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserEntity that = (UserEntity) o;
        return email != null ? email.equals(that.email) : that.email == null;
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
