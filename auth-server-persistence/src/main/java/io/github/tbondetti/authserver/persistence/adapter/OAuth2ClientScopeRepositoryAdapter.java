package io.github.tbondetti.authserver.persistence.adapter;

import io.github.tbondetti.authserver.core.port.OAuth2ClientScopeRepositoryPort;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientScopeEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientScopeId;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ScopeEntity;
import io.github.tbondetti.authserver.persistence.repository.OAuth2ClientJpaRepository;
import io.github.tbondetti.authserver.persistence.repository.OAuth2ClientScopeJpaRepository;
import io.github.tbondetti.authserver.persistence.repository.OAuth2ScopeJpaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuth2ClientScopeRepositoryAdapter implements OAuth2ClientScopeRepositoryPort {

    private final OAuth2ClientScopeJpaRepository oauth2ClientScopeJpaRepository;
    private final OAuth2ClientJpaRepository oauth2ClientJpaRepository;
    private final OAuth2ScopeJpaRepository oauth2ScopeJpaRepository;

    @Override
    public boolean exists(
            final String applicationCode,
            final String code,
            final String clientId
    ) {
        return this.oauth2ClientScopeJpaRepository.findByApplicationCodeAndCodeAndClientId(
                applicationCode,
                code,
                clientId
        ).isPresent();
    }

    @Override
    public void assign(
            final String applicationCode,
            final String code,
            final String clientId
    ) {
        final OAuth2ScopeEntity scope = this.oauth2ScopeJpaRepository.getByApplicationCodeAndCode(applicationCode, code);
        final OAuth2ClientEntity client = this.oauth2ClientJpaRepository.getByClientId(clientId);

        final OAuth2ClientScopeId newId = new OAuth2ClientScopeId(
                client.getId(),
                scope.getId()
        );

        this.oauth2ClientScopeJpaRepository.save(new OAuth2ClientScopeEntity(
                newId,
                client,
                scope
        ));

    }
}
