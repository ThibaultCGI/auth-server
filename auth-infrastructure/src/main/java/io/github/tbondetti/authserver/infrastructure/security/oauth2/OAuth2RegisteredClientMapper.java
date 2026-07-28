package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.List;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

@UtilityClass
public class OAuth2RegisteredClientMapper {

    public static RegisteredClient toRegisteredClient(
            final OAuth2Client client,
            final List<OAuth2Scope> scopes
    ) {
        final RegisteredClient.Builder builder = RegisteredClient.withId(client.id().toString())
                .clientId(client.clientId())
                .clientSecret(client.clientSecretHash())
                .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                .authorizationGrantType(CLIENT_CREDENTIALS);

        scopes.stream().map(OAuth2Scope::completeCode).forEach(builder::scope);

        return builder.build();
    }
}