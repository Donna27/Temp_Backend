package com.kbtg.tempbackend.service;

import com.kbtg.tempbackend.interfaces.dtos.LoginResponse;
import com.kbtg.tempbackend.interfaces.dtos.UserProfileResponse;
import com.kbtg.tempbackend.interfaces.dtos.UserRegistrationResponse;
import com.kbtg.tempbackend.application.services.UserApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTestClean {

    @Autowired
    private UserApplicationService userApplicationService;

    @Test
    void contextLoads() {
        // This test ensures that the Spring context loads successfully with our Clean Architecture
        assertNotNull(userApplicationService);
    }
}
