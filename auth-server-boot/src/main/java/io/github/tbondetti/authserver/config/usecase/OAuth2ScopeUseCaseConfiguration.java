package io.github.tbondetti.authserver.config.usecase;

import io.github.tbondetti.authserver.core.port.OAuth2ClientScopeRepositoryPort;
import io.github.tbondetti.authserver.core.port.OAuth2ScopeRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2client.GetOAuth2ClientUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2scope.AssignOAuth2ScopesToOAuth2ClientUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2scope.CreateOAuth2ScopeUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2scope.GetOAuth2ScopeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2ScopeUseCaseConfiguration {

    @Bean
    GetOAuth2ScopeUseCase getOAuth2ScopeUseCase(
            final OAuth2ScopeRepositoryPort oauth2ScopeRepositoryPort,
            final GetApplicationUseCase getApplicationUseCase
    ) {
        return new GetOAuth2ScopeUseCase(oauth2ScopeRepositoryPort, getApplicationUseCase);
    }

    @Bean
    CreateOAuth2ScopeUseCase createOAuth2ScopeUseCase(
            final OAuth2ScopeRepositoryPort oauth2ScopeRepositoryPort,
            final GetApplicationUseCase getApplicationUseCase
    ) {
        return new CreateOAuth2ScopeUseCase(
                oauth2ScopeRepositoryPort,
                getApplicationUseCase
        );
    }

    @Bean
    AssignOAuth2ScopesToOAuth2ClientUseCase assignOAuth2ScopesToOAuth2ClientUseCase(
            final OAuth2ClientScopeRepositoryPort oauth2ClientScopeRepositoryPort,
            final GetOAuth2ScopeUseCase getOAuth2ScopeUseCase,
            final GetOAuth2ClientUseCase getOAuth2ClientUseCase
    ) {
        return new AssignOAuth2ScopesToOAuth2ClientUseCase(
                oauth2ClientScopeRepositoryPort,
                getOAuth2ScopeUseCase,
                getOAuth2ClientUseCase
        );
    }
}
