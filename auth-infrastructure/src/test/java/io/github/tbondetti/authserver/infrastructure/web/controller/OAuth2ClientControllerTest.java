package io.github.tbondetti.authserver.infrastructure.web.controller;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.infrastructure.service.OAuth2ClientService;
import io.github.tbondetti.authserver.infrastructure.web.dto.CreateOAuth2ClientRequest;
import io.github.tbondetti.authserver.infrastructure.web.mapper.OAuth2ClientWebMapper;
import io.github.tbondetti.authserver.infrastructure.web.response.CreateOAuth2ClientResponse;
import io.github.tbondetti.authserver.infrastructure.web.response.OAuth2ClientResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.OAuth2ClientWebMapper.toCreateResponse;
import static io.github.tbondetti.authserver.infrastructure.web.mapper.OAuth2ClientWebMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ClientControllerTest {

    @InjectMocks
    private OAuth2ClientController subject;

    @Mock
    private OAuth2ClientService oauth2ClientService;

    @Test
    void getOAuth2ClientOk() {
        final String clientId = "clientId";
        final OAuth2Client client = OAuth2Client.builder().build();
        when(this.oauth2ClientService.getOAuth2Client(clientId)).thenReturn(client);

        try (final MockedStatic<OAuth2ClientWebMapper> utilities = mockStatic(OAuth2ClientWebMapper.class)) {
            final OAuth2ClientResponse expected = OAuth2ClientResponse.builder().build();
            utilities.when(() -> toResponse(client)).thenReturn(expected);

            assertSame(expected, this.subject.getOAuth2Client(clientId));
        }
    }

    @Test
    void createOAuth2ClientOk() {
        final String clientName = "clientName";
        final String applicationCode = "applicationCode";
        final CreateOAuth2ClientRequest request = new CreateOAuth2ClientRequest(
                clientName,
                applicationCode
        );

        final OAuth2CreatedClient client = OAuth2CreatedClient.builder().build();
        when(this.oauth2ClientService.createOAuth2Client(
                clientName,
                applicationCode
        )).thenReturn(client);

        try (final MockedStatic<OAuth2ClientWebMapper> utilities = mockStatic(OAuth2ClientWebMapper.class)) {
            final CreateOAuth2ClientResponse expected = CreateOAuth2ClientResponse.builder().build();
            utilities.when(() -> toCreateResponse(client)).thenReturn(expected);

            assertSame(expected, this.subject.createOAuth2Client(request));
        }
    }
}