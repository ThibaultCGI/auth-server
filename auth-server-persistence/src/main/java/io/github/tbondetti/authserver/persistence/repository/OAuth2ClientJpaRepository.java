package io.github.tbondetti.authserver.persistence.repository;

import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OAuth2ClientJpaRepository extends JpaRepository<OAuth2ClientEntity, UUID> {

    /* on force le chargement de l'application (qui est lazy sinon) */
    @EntityGraph(attributePaths = { "application", "grantTypes", "redirectUris" })
    OAuth2ClientEntity getByClientId(@Param("clientId") final String clientId);

    @Nonnull
    @EntityGraph(attributePaths = { "application", "grantTypes", "redirectUris" })
    Optional<OAuth2ClientEntity> findById(@Nonnull final UUID id);

    @EntityGraph(attributePaths = { "application", "grantTypes", "redirectUris" })
    Optional<OAuth2ClientEntity> findByClientId(@Param("clientId") final String clientId);

}
