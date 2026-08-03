package io.github.tbondetti.authserver.web.service;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.core.usecase.oauth2client.CreateOAuth2ClientUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2client.GetOAuth2ClientUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2scope.AssignOAuth2ScopesToOAuth2ClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuth2ClientService {

    private final GetOAuth2ClientUseCase getOAuth2ClientUseCase;
    private final CreateOAuth2ClientUseCase createOAuth2ClientUseCase;
    private final AssignOAuth2ScopesToOAuth2ClientUseCase assignOAuth2ScopesToOAuth2ClientUseCase;

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

    @Transactional
    public void assignScope(
            final String clientId,
            final String applicationCode,
            final String code
    ) {
        this.assignOAuth2ScopesToOAuth2ClientUseCase.execute(
                applicationCode,
                code,
                clientId
        );
    }
}
