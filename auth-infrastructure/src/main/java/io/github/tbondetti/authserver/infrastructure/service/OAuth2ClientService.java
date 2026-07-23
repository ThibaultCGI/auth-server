package io.github.tbondetti.authserver.infrastructure.service;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.core.usecase.oauth2client.CreateOAuth2ClientUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2client.GetOAuth2ClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuth2ClientService {

    private final GetOAuth2ClientUseCase getOAuth2ClientUseCase;
    private final CreateOAuth2ClientUseCase createOAuth2ClientUseCase;

    public OAuth2Client getOAuth2Client(final String clientId) {
        return this.getOAuth2ClientUseCase.execute(clientId);
    }

    @Transactional
    public OAuth2CreatedClient createOAuth2Client(
            final String clientName,
            final String applicationCode
    ) {
        return this.createOAuth2ClientUseCase.execute(
                clientName,
                applicationCode
        );
    }
}
