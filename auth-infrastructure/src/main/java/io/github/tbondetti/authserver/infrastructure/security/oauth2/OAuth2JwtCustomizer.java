package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@RequiredArgsConstructor
public class OAuth2JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    static final String CLAIM_NAME_CLIENT_ID = "client_id";
    static final String CLAIM_NAME_APPLICATION_CODE = "application_code";

    private final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;

    @Override
    public void customize(final JwtEncodingContext context) {
        final String clientId = context.getRegisteredClient().getClientId();
        this.oauth2ClientRepositoryPort.findByClientId(clientId).ifPresent(
                client -> addClientClaims(context, client)
        );
    }

    private static void addClientClaims(
            final JwtEncodingContext context,
            final OAuth2Client client
    ) {
        context.getClaims()
                .claim(CLAIM_NAME_CLIENT_ID, client.clientId())
                .claim(CLAIM_NAME_APPLICATION_CODE, client.applicationCode())
                ;
    }
}
