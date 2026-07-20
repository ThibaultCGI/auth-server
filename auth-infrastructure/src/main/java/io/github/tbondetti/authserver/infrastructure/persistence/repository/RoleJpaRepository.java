package io.github.tbondetti.authserver.infrastructure.persistence.repository;

import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID> {

    RoleEntity getByApplicationAndCode(
            final ApplicationEntity application,
            final String code
    );

    Optional<RoleEntity> findByCodeAndApplication(
            final String code,
            final ApplicationEntity applicationEntity
    );

}
