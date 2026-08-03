package io.github.tbondetti.authserver.persistence.repository;

import io.github.tbondetti.authserver.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(final String username);
    UserEntity getByUsername(final String username);
}
