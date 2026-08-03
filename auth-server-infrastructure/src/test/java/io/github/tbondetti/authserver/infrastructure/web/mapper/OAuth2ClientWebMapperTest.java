package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.infrastructure.web.response.CreateOAuth2ClientResponse;
import io.github.tbondetti.authserver.infrastructure.web.response.OAuth2ClientResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.OAuth2ClientWebMapper.toCreateResponse;
import static io.github.tbondetti.authserver.infrastructure.web.mapper.OAuth2ClientWebMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OAuth2ClientWebMapperTest {

    @Test
    void toResponseOK() {
        final String cliendId = "cliendId";
        final String clientName = "clientName";
        final String applicationCode = "applicationCode";

        final OAuth2Client given = OAuth2Client.builder()
                .id(UUID.randomUUID())
                .clientId(cliendId)
                .clientName(clientName)
                .clientSecretHash("clientSecretHash")
                .applicationCode(applicationCode)
                .build();

        final OAuth2ClientResponse expected = OAuth2ClientResponse.builder()
                .clientId(cliendId)
                .clientName(clientName)
                .applicationCode(applicationCode)
                .build();

        assertEquals(expected, toResponse(given));
    }

    @Test
    void toCreateResponseOk() {
        final String cliendId = "cliendId";
        final String clientName = "clientName";
        final String clientSecret = "clientSecret";
        final String applicationCode = "applicationCode";

        final OAuth2CreatedClient given = OAuth2CreatedClient.builder()
                .id(UUID.randomUUID())
                .clientId(cliendId)
                .clientName(clientName)
                .clientSecret(clientSecret)
                .applicationCode(applicationCode)
                .build();

        final CreateOAuth2ClientResponse expected = CreateOAuth2ClientResponse.builder()
                .clientId(cliendId)
                .clientName(clientName)
                .clientSecret(clientSecret)
                .applicationCode(applicationCode)
                .build();

        assertEquals(expected, toCreateResponse(given));
    }
}