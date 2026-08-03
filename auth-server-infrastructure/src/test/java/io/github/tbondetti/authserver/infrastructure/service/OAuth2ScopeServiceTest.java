package io.github.tbondetti.authserver.infrastructure.service;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.usecase.oauth2scope.CreateOAuth2ScopeUseCase;
import io.github.tbondetti.authserver.core.usecase.oauth2scope.GetOAuth2ScopeUseCase;
import io.github.tbondetti.authserver.web.service.OAuth2ScopeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ScopeServiceTest {

    @InjectMocks
    private OAuth2ScopeService subject;

    @Mock
    private GetOAuth2ScopeUseCase getOAuth2ScopeUseCase;

    @Mock
    private CreateOAuth2ScopeUseCase createOAuth2ScopeUseCase;


    @Test
    void getOAuth2ScopeOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";

        final OAuth2Scope expected = OAuth2Scope.builder().build();

        when(this.getOAuth2ScopeUseCase.execute(applicationCode, code)).thenReturn(expected);

        assertSame(expected, this.subject.getOAuth2Scope(applicationCode, code));
    }

    @Test
    void createOAuth2ScopeOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final OAuth2Scope expected = OAuth2Scope.builder().build();

        when(this.createOAuth2ScopeUseCase.execute(
                applicationCode,
                code,
                name,
                description
        )).thenReturn(expected);

        assertSame(expected, this.subject.createOAuth2Scope(applicationCode, code, name, description));
    }
}