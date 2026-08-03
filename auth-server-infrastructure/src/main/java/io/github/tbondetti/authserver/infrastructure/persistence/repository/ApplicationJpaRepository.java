package io.github.tbondetti.authserver.infrastructure.persistence.repository;

import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationJpaRepository extends JpaRepository<ApplicationEntity, UUID> {

    Optional<ApplicationEntity> findByCode(final String code);

    ApplicationEntity getByCode(final String code);
}
