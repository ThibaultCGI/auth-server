package io.github.tbondetti.authserver.infrastructure.config.usecase;

import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2client.CreateOAuth2ClientUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2client.GetOauth2ClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class OAuth2ClientUseCase {

    @Bean
    GetOauth2ClientUseCase getOauth2ClientUseCase(final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort) {
        return new GetOauth2ClientUseCase(oauth2ClientRepositoryPort);
    }

    @Bean
    CreateOAuth2ClientUseCase createOAuth2ClientUseCase(
            final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort,
            final PasswordEncoderPort passwordEncoderPort,
            final GetApplicationUseCase getApplicationUseCase
    ) {
        return new CreateOAuth2ClientUseCase(
                oauth2ClientRepositoryPort,
                passwordEncoderPort,
                getApplicationUseCase
        );
    }

}
