package io.github.tbondetti.authserver.web.facade;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.usecase.oauth2scope.CreateOAuth2ScopeUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2scope.GetOAuth2ScopeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuth2ScopeFacade {

    private final GetOAuth2ScopeUseCase getOAuth2ScopeUseCase;
    private final CreateOAuth2ScopeUseCase createOAuth2ScopeUseCase;

    public OAuth2Scope getOAuth2Scope(
            final String applicationCode,
            final String code
    ) {
        return this.getOAuth2ScopeUseCase.execute(applicationCode, code);
    }

    @Transactional
    public OAuth2Scope createOAuth2Scope(
            final String applicationCode,
            final String code,
            final String name,
            final String description
    ) {
        return this.createOAuth2ScopeUseCase.execute(
                applicationCode,
                code,
                name,
                description
        );
    }
}
