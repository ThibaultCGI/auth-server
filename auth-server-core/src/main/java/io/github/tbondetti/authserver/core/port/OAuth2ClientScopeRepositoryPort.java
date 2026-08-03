package io.github.tbondetti.authserver.core.port;

public interface OAuth2ClientScopeRepositoryPort {

    boolean exists(
            final String applicationCode,
            final String code,
            final String clientId
    );

    void assign(
            final String applicationCode,
            final String code,
            final String clientId
    );
}
