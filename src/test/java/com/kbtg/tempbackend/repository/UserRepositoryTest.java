package com.kbtg.tempbackend.repository;

import com.kbtg.tempbackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setPhoneNumber("1234567890");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void save_User_Success() {
        // When
        User savedUser = userRepository.save(testUser);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        assertEquals(testUser.getFirstname(), savedUser.getFirstname());
        assertEquals(testUser.getLastname(), savedUser.getLastname());
        assertEquals(testUser.getPhoneNumber(), savedUser.getPhoneNumber());
        assertEquals(testUser.getBirthday(), savedUser.getBirthday());
    }

    @Test
    void findByEmail_UserExists() {
        // Given
        userRepository.save(testUser);

        // When
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
        assertEquals(testUser.getFirstname(), foundUser.get().getFirstname());
    }

    @Test
    void findByEmail_UserDoesNotExist() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void existsByEmail_UserExists() {
        // Given
        userRepository.save(testUser);

        // When
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByEmail_UserDoesNotExist() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    void deleteAll_RemovesAllUsers() {
        // Given
        userRepository.save(testUser);
        
        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("password456");
        anotherUser.setFirstname("Jane");
        anotherUser.setLastname("Smith");
        anotherUser.setPhoneNumber("0987654321");
        anotherUser.setBirthday(LocalDate.of(1995, 5, 15));
        userRepository.save(anotherUser);

        assertEquals(2, userRepository.count());

        // When
        userRepository.deleteAll();

        // Then
        assertEquals(0, userRepository.count());
    }

    @Test
    void count_ReturnsCorrectCount() {
        // Given
        assertEquals(0, userRepository.count());

        userRepository.save(testUser);
        assertEquals(1, userRepository.count());

        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("password456");
        anotherUser.setFirstname("Jane");
        anotherUser.setLastname("Smith");
        anotherUser.setPhoneNumber("0987654321");
        anotherUser.setBirthday(LocalDate.of(1995, 5, 15));
        userRepository.save(anotherUser);

        // When & Then
        assertEquals(2, userRepository.count());
    }

    @Test
    void findByEmail_CaseSensitive() {
        // Given
        userRepository.save(testUser);

        // When
        Optional<User> foundUser1 = userRepository.findByEmail("test@example.com");
        Optional<User> foundUser2 = userRepository.findByEmail("TEST@EXAMPLE.COM");

        // Then
        assertTrue(foundUser1.isPresent());
        assertTrue(foundUser2.isEmpty()); // Email should be case-sensitive
    }
}
