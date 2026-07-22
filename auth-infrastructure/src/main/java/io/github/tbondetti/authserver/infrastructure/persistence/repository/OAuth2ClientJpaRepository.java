package io.github.tbondetti.authserver.infrastructure.persistence.repository;

import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OAuth2ClientJpaRepository extends JpaRepository<OAuth2ClientEntity, UUID> {

    /* on force le chargement de l'application (qui est lazy sinon) */
    String FIND_BY_CLIENT_ID_QUERY =
            """
            SELECT
                oauth
            FROM OAuth2ClientEntity oauth
            JOIN FETCH oauth.application
            WHERE oauth.client_id = :clientId
            """;

    @Query(FIND_BY_CLIENT_ID_QUERY)
    OAuth2ClientEntity getByClientId(@Param("clientId") final String clientId);

    @Query(FIND_BY_CLIENT_ID_QUERY)
    Optional<OAuth2ClientEntity> findByClientId(@Param("clientId") final String clientId);

}
