package io.github.tbondetti.authserver.core.port;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;

import java.util.Optional;
import java.util.UUID;

public interface OAuth2ClientRepositoryPort {

    OAuth2Client save(final OAuth2Client client);

    Optional<OAuth2Client> findById(final UUID id);

    Optional<OAuth2Client> findByClientId(final String clientId);
}