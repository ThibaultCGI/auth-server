package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.security.oauth2.OAuth2RegisteredClientMapper.DEFAULT_SCOPE;
import static io.github.tbondetti.authserver.infrastructure.security.oauth2.OAuth2RegisteredClientMapper.toRegisteredClient;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

class OAuth2RegisteredClientMapperTest {


    @Test
    void toRegisteredClientOk() {
        final UUID id = randomUUID();
        final String cliendId = "cliendId";
        final String clientSecretHash = "clientSecretHash";
        final OAuth2Client client = OAuth2Client.builder()
                .id(id)
                .clientId(cliendId)
                .clientSecretHash(clientSecretHash)
                .build();

        final RegisteredClient expected = RegisteredClient.withId(client.id().toString())
                .clientId(client.clientId())
                .clientSecret(client.clientSecretHash())
                .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                .authorizationGrantType(CLIENT_CREDENTIALS)
                .scope(DEFAULT_SCOPE)
                .build();

        assertEquals(expected, toRegisteredClient(client));
    }
}