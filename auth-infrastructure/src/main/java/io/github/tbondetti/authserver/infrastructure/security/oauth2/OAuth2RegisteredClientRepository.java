package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class OAuth2RegisteredClientRepository implements RegisteredClientRepository {

    static final String ERROR_DO_NOT_SAVE = "OAuth2 clients must be created through the application API.";
    private final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;

    @Override
    public void save(@Nonnull final RegisteredClient registeredClient) {
        throw new UnsupportedOperationException(ERROR_DO_NOT_SAVE);
    }

    @Override
    public RegisteredClient findById(@Nonnull final String id) {
        return this.oauth2ClientRepositoryPort
                .findById(UUID.fromString(id))
                .map(OAuth2RegisteredClientMapper::toRegisteredClient)
                .orElse(null);
    }


    @Override
    public RegisteredClient findByClientId(@Nonnull final String clientId) {
        // on n'utilise pas GetOAuth2ClientUseCase car Spring ne s'attend pas à recevoir une exception fonctionnelle,
        // mais null si aucun client trouvé
        return this.oauth2ClientRepositoryPort.findByClientId(clientId)
                .map(OAuth2RegisteredClientMapper::toRegisteredClient)
                .orElse(null);
    }
}
