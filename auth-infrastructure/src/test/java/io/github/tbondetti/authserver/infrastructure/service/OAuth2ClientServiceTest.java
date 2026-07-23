package io.github.tbondetti.authserver.infrastructure.service;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.core.usecase.oauth2client.CreateOAuth2ClientUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2client.GetOAuth2ClientUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ClientServiceTest {

    @InjectMocks
    private OAuth2ClientService subject;

    @Mock
    private GetOAuth2ClientUseCase getOAuth2ClientUseCase;

    @Mock
    private CreateOAuth2ClientUseCase createOAuth2ClientUseCase;

    @Test
    void getOAuth2ClientOk() {
        final String clientId = "clientId";
        final OAuth2Client client = OAuth2Client.builder().build();

        when(this.getOAuth2ClientUseCase.execute(clientId)).thenReturn(client);

        assertSame(client, this.subject.getOAuth2Client(clientId));
    }

    @Test
    void createOAuth2ClientOk() {
        final String clientName = "clientName";
        final String applicationCode = "applicationCode";

        final OAuth2CreatedClient client = OAuth2CreatedClient.builder().build();

        when(this.createOAuth2ClientUseCase.execute(
                clientName,
                applicationCode
        )).thenReturn(client);

        assertSame(client, this.subject.createOAuth2Client(clientName, applicationCode));
    }

}