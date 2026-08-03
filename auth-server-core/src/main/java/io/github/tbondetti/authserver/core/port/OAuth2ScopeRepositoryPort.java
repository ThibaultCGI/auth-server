package io.github.tbondetti.authserver.core.port;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;

import java.util.Optional;

public interface OAuth2ScopeRepositoryPort {

    OAuth2Scope save(final OAuth2Scope scope);

    Optional<OAuth2Scope> findByApplicationCodeAndCode(
            final String applicationCode,
            final String code
    );
}