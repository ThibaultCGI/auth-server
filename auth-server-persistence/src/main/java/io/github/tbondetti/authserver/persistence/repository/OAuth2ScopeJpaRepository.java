package io.github.tbondetti.authserver.persistence.repository;

import io.github.tbondetti.authserver.persistence.entity.OAuth2ScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OAuth2ScopeJpaRepository extends JpaRepository<OAuth2ScopeEntity, UUID> {

    /* on force le chargement de l'application (qui est lazy sinon) */
    String FIND_BY_APPLICATION_AND_CODE_QUERY =
            """
            SELECT
                s
            FROM OAuth2ScopeEntity s
            JOIN FETCH s.application
            WHERE s.code = :code
                AND s.application.code = :applicationCode
            """;

    /* on force le chargement de l'application (qui est lazy sinon) */
    @Query(FIND_BY_APPLICATION_AND_CODE_QUERY)
    OAuth2ScopeEntity getByApplicationCodeAndCode(
            @Param("applicationCode") final String applicationCode,
            @Param("code") final String code
    );

    @Query(FIND_BY_APPLICATION_AND_CODE_QUERY)
    Optional<OAuth2ScopeEntity> findByApplicationCodeAndCode(
            @Param("applicationCode") final String applicationCode,
            @Param("code") final String code
    );

    @Query(
            """
            SELECT
                cs.scope
            FROM OAuth2ClientScopeEntity cs
            JOIN FETCH cs.scope.application
            WHERE cs.client.clientId = :clientId
            """
    )
    List<OAuth2ScopeEntity> findAllByClientId(@Param("clientId") final String clientId);

}
