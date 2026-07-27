package io.github.tbondetti.authserver.infrastructure.persistence.repository;

import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ClientScopeEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ClientScopeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuth2ClientScopeJpaRepository extends JpaRepository<OAuth2ClientScopeEntity, OAuth2ClientScopeId> {
    @Query(
            """
            SELECT
                cs
            FROM OAuth2ClientScopeEntity cs
            WHERE cs.scope.application.code = :applicationCode
                AND cs.scope.code = :code
                AND cs.client.clientId = :clientId
            """
    )
    Optional<OAuth2ClientScopeEntity> findByApplicationCodeAndCodeAndClientId(
            @Param("applicationCode") final String applicationCode,
            @Param("code") final String code,
            @Param("clientId") final String clientId
    );
}
