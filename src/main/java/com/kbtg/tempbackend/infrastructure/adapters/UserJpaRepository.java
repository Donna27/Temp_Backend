package com.kbtg.tempbackend.infrastructure.adapters;

import com.kbtg.tempbackend.infrastructure.entities.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * User JPA Repository - Spring Data JPA repository interface
 * Handles database operations for UserJpaEntity
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    
    Optional<UserJpaEntity> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
