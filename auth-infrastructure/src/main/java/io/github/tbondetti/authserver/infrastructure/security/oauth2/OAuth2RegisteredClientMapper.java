package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

@UtilityClass
public class OAuth2RegisteredClientMapper {

    static final String DEFAULT_SCOPE = "default";

    public static RegisteredClient toRegisteredClient(
            final OAuth2Client client
    ) {

        return RegisteredClient.withId(client.id().toString())
                .clientId(client.clientId())
                .clientSecret(client.clientSecretHash())
                .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                .authorizationGrantType(CLIENT_CREDENTIALS)
                .scope(DEFAULT_SCOPE)
                .build();
    }
}