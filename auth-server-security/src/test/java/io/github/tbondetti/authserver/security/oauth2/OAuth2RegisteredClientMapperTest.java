package io.github.tbondetti.authserver.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.CLIENT_CREDENTIALS;
import static io.github.tbondetti.authserver.security.oauth2.OAuth2RegisteredClientMapper.toRegisteredClient;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

class OAuth2RegisteredClientMapperTest {

    @Test
    void toRegisteredClientOk() {
        final UUID id = randomUUID();
        final String clientId = "clientId";
        final String clientSecretHash = "clientSecretHash";

        final OAuth2Client client = OAuth2Client.builder()
                .id(id)
                .clientId(clientId)
                .clientSecretHash(clientSecretHash)
                .grantTypes(Set.of(CLIENT_CREDENTIALS))
                .redirectUris(Set.of(URI.create("uri")))
                .build();

        final OAuth2Scope scope1 = OAuth2Scope.builder()
                .applicationCode("tas1")
                .code("users.read")
                .build();

        final OAuth2Scope scope2 = OAuth2Scope.builder()
                .applicationCode("tas2")
                .code("users.write")
                .build();

        final RegisteredClient expected = RegisteredClient.withId(id.toString())
                .clientId(clientId)
                .clientSecret(clientSecretHash)
                .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("uri")
                .scope("tas1:users.read")
                .scope("tas2:users.write")
                .build();

        assertEquals(expected, toRegisteredClient(client, List.of(scope1, scope2)));

        assertEquals(
                Set.of(scope1.completeCode(), scope2.completeCode()),
                toRegisteredClient(client, List.of(scope1, scope2)).getScopes()
        );
    }
}