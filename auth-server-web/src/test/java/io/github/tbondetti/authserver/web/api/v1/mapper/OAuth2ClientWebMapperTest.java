package io.github.tbondetti.authserver.web.api.v1.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType;
import io.github.tbondetti.authserver.web.api.v1.response.CreateOAuth2ClientResponse;
import io.github.tbondetti.authserver.web.api.v1.response.OAuth2ClientResponse;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.CLIENT_CREDENTIALS;
import static io.github.tbondetti.authserver.web.api.v1.mapper.OAuth2ClientWebMapper.toCreateResponse;
import static io.github.tbondetti.authserver.web.api.v1.mapper.OAuth2ClientWebMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OAuth2ClientWebMapperTest {

    @Test
    void toResponseOK() {
        final String cliendId = "cliendId";
        final String clientName = "clientName";
        final String applicationCode = "applicationCode";
        final Set<OAuth2ClientGrantType> grantTypes = Set.of(CLIENT_CREDENTIALS);
        final Set<URI> redirectUris = Set.of(URI.create("uri"));

        final OAuth2Client given = OAuth2Client.builder()
                .id(UUID.randomUUID())
                .clientId(cliendId)
                .clientName(clientName)
                .clientSecretHash("clientSecretHash")
                .applicationCode(applicationCode)
                .grantTypes(grantTypes)
                .redirectUris(redirectUris)
                .build();

        final OAuth2ClientResponse expected = OAuth2ClientResponse.builder()
                .clientId(cliendId)
                .clientName(clientName)
                .applicationCode(applicationCode)
                .grantTypes(Set.of(CLIENT_CREDENTIALS.getValue()))
                .redirectUris(Set.of("uri"))
                .build();

        assertEquals(expected, toResponse(given));
    }

    @Test
    void toCreateResponseOk() {
        final String cliendId = "cliendId";
        final String clientName = "clientName";
        final String clientSecret = "clientSecret";
        final String applicationCode = "applicationCode";
        final Set<OAuth2ClientGrantType> grantTypes = Set.of(CLIENT_CREDENTIALS);
        final Set<URI> redirectUris = Set.of(URI.create("uri"));

        final OAuth2CreatedClient given = OAuth2CreatedClient.builder()
                .id(UUID.randomUUID())
                .clientId(cliendId)
                .clientName(clientName)
                .clientSecret(clientSecret)
                .applicationCode(applicationCode)
                .grantTypes(grantTypes)
                .redirectUris(redirectUris)
                .build();

        final CreateOAuth2ClientResponse expected = CreateOAuth2ClientResponse.builder()
                .clientId(cliendId)
                .clientName(clientName)
                .clientSecret(clientSecret)
                .applicationCode(applicationCode)
                .grantTypes(Set.of(CLIENT_CREDENTIALS.getValue()))
                .redirectUris(Set.of("uri"))
                .build();

        assertEquals(expected, toCreateResponse(given));
    }
}