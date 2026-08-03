package io.github.tbondetti.authserver.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.core.port.OAuth2ScopeRepositoryPort;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.List;
import java.util.UUID;

import static io.github.tbondetti.authserver.security.oauth2.OAuth2RegisteredClientMapper.toRegisteredClient;

@RequiredArgsConstructor
public class OAuth2RegisteredClientRepository implements RegisteredClientRepository {

    static final String ERROR_DO_NOT_SAVE = "OAuth2 clients must be created through the application API.";
    private final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;
    private final OAuth2ScopeRepositoryPort oauth2ScopeRepositoryPort;

    @Override
    public void save(@Nonnull final RegisteredClient registeredClient) {
        throw new UnsupportedOperationException(ERROR_DO_NOT_SAVE);
    }

    @Override
    public RegisteredClient findById(@Nonnull final String id) {

        return this.oauth2ClientRepositoryPort
                .findById(UUID.fromString(id))
                .map(this::toClient)
                .orElse(null);
    }


    @Override
    public RegisteredClient findByClientId(@Nonnull final String clientId) {
        // on n'utilise pas GetOAuth2ClientUseCase car Spring ne s'attend pas à recevoir une exception fonctionnelle,
        // mais null si aucun client trouvé
        return this.oauth2ClientRepositoryPort.findByClientId(clientId)
                .map(this::toClient)
                .orElse(null);
    }

    RegisteredClient toClient(final OAuth2Client client) {
        final List<OAuth2Scope> scopes = this.oauth2ScopeRepositoryPort.findAllByClientId(client.clientId());

        return toRegisteredClient(client, scopes);
    }
}
