package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.port.OAuth2ScopeRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ScopeEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ScopeMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.OAuth2ScopeJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ScopeMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ScopeMapper.toEntity;

@RequiredArgsConstructor
public class OAuth2ScopeRepositoryAdapter implements OAuth2ScopeRepositoryPort {

    private final OAuth2ScopeJpaRepository oauth2ScopeJpaRepository;
    private final ApplicationJpaRepository applicationJpaRepository;

    @Override
    public Optional<OAuth2Scope> findByApplicationCodeAndCode(
            final String applicationCode,
            final String code
    ) {
        return this.oauth2ScopeJpaRepository.findByApplicationCodeAndCode(
                applicationCode,
                code
        ).map(OAuth2ScopeMapper::toDomain);
    }

    @Override
    public OAuth2Scope save(final OAuth2Scope scope) {
        final ApplicationEntity application = this.applicationJpaRepository.getByCode(scope.applicationCode());
        final OAuth2ScopeEntity toSave = toEntity(scope, application);
        final OAuth2ScopeEntity saved = this.oauth2ScopeJpaRepository.save(toSave);

        return toDomain(this.oauth2ScopeJpaRepository.getByApplicationCodeAndCode(saved.getApplication().getCode(), saved.getCode()));
    }
}
