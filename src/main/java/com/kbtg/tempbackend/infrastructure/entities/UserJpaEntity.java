package com.kbtg.tempbackend.infrastructure.entities;

import com.kbtg.tempbackend.domain.entities.UserEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * User JPA Entity - Infrastructure layer entity for database persistence
 * This entity is responsible for JPA/Database mapping only
 * Contains no business logic - only persistence concerns
 */
@Entity
@Table(name = "users")
public class UserJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String firstname;
    
    @Column(nullable = false)
    private String lastname;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private LocalDate birthday;

    // Default constructor (required by JPA)
    public UserJpaEntity() {}

    // Constructor for creating from domain entity
    public UserJpaEntity(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.firstname = userEntity.getFirstname();
        this.lastname = userEntity.getLastname();
        this.phoneNumber = userEntity.getPhoneNumber();
        this.birthday = userEntity.getBirthday();
    }

    /**
     * Convert JPA entity to domain entity
     * @return UserEntity domain object
     */
    public UserEntity toDomainEntity() {
        return new UserEntity(id, email, password, firstname, lastname, phoneNumber, birthday);
    }

    /**
     * Update JPA entity from domain entity
     * @param userEntity The domain entity to update from
     */
    public void updateFromDomainEntity(UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.firstname = userEntity.getFirstname();
        this.lastname = userEntity.getLastname();
        this.phoneNumber = userEntity.getPhoneNumber();
        this.birthday = userEntity.getBirthday();
    }

    // Getters and Setters (required by JPA)
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
