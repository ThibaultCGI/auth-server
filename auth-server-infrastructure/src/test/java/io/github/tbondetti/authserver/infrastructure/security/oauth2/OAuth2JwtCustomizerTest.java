package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.security.oauth2.OAuth2JwtCustomizer.CLAIM_NAME_APPLICATION_CODE;
import static io.github.tbondetti.authserver.infrastructure.security.oauth2.OAuth2JwtCustomizer.CLAIM_NAME_CLIENT_ID;
import static io.github.tbondetti.authserver.infrastructure.security.oauth2.OAuth2JwtCustomizer.addClientClaims;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

@ExtendWith(MockitoExtension.class)
class OAuth2JwtCustomizerTest {

    @Spy
    @InjectMocks
    private OAuth2JwtCustomizer subject;

    @Mock
    private OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;

    @Mock
    private JwtEncodingContext context;

    @Mock
    private JwtClaimsSet.Builder claimsBuilder;

    @Test
    void addClientClaimsOk() {
        final String clientId = "clientId";
        final String applicationCode = "applicationCode";
        final OAuth2Client client = OAuth2Client.builder()
                .clientId(clientId)
                .applicationCode(applicationCode)
                .build();

        when(this.context.getClaims()).thenReturn(this.claimsBuilder);
        when(this.claimsBuilder.claim(CLAIM_NAME_CLIENT_ID, clientId)).thenReturn(this.claimsBuilder);
        when(this.claimsBuilder.claim(CLAIM_NAME_APPLICATION_CODE, applicationCode)).thenReturn(this.claimsBuilder);

        addClientClaims(this.context, client);

        verify(this.context, times(1)).getClaims();
        verify(this.claimsBuilder, times(1)).claim(CLAIM_NAME_CLIENT_ID, clientId);
        verify(this.claimsBuilder, times(1)).claim(CLAIM_NAME_APPLICATION_CODE, applicationCode);
    }

    @Test
    void customizeOk() {
        final String clientId = "clientId";

        final RegisteredClient registeredClient = RegisteredClient.withId("idRegisteredClient")
                .clientId(clientId)
                .authorizationGrantType(CLIENT_CREDENTIALS)
                .build();
        when(this.context.getRegisteredClient()).thenReturn(registeredClient);

        when(this.oauth2ClientRepositoryPort.findByClientId(clientId)).thenReturn(Optional.empty());

        try (MockedStatic<OAuth2JwtCustomizer> utilities = mockStatic(OAuth2JwtCustomizer.class, CALLS_REAL_METHODS)) {

            this.subject.customize(this.context);

            utilities.verify(
                    () -> addClientClaims(any(), any()),
                    times(0)
            );
        }
    }

    @Test
    void customizeOk2() {
        final String clientId = "clientId";

        final RegisteredClient registeredClient = RegisteredClient.withId("idRegisteredClient")
                .clientId(clientId)
                .authorizationGrantType(CLIENT_CREDENTIALS)
                .build();
        when(this.context.getRegisteredClient()).thenReturn(registeredClient);

        final OAuth2Client client = OAuth2Client.builder().build();
        when(this.oauth2ClientRepositoryPort.findByClientId(clientId)).thenReturn(Optional.of(client));

        try (MockedStatic<OAuth2JwtCustomizer> utilities = mockStatic(OAuth2JwtCustomizer.class)) {
            this.subject.customize(this.context);

            utilities.verify(
                    () -> addClientClaims(this.context, client),
                    times(1)
            );
        }
    }
}
