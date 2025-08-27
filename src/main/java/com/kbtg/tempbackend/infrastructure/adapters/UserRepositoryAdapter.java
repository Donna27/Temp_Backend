package com.kbtg.tempbackend.infrastructure.adapters;

import com.kbtg.tempbackend.domain.entities.UserEntity;
import com.kbtg.tempbackend.domain.repositories.UserRepositoryPort;
import com.kbtg.tempbackend.infrastructure.entities.UserJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Repository Adapter - Infrastructure adapter implementing domain repository port
 * Bridges between domain layer and Spring Data JPA
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    
    private final UserJpaRepository jpaRepository;
    
    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public UserEntity save(UserEntity user) {
        UserJpaEntity jpaEntity = new UserJpaEntity(user);
        UserJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return savedEntity.toDomainEntity();
    }
    
    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(UserJpaEntity::toDomainEntity);
    }
    
    @Override
    public Optional<UserEntity> findById(Long id) {
        return jpaRepository.findById(id)
                .map(UserJpaEntity::toDomainEntity);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
    
    @Override
    public List<UserEntity> findAll() {
        return jpaRepository.findAll().stream()
                .map(UserJpaEntity::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public long count() {
        return jpaRepository.count();
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
