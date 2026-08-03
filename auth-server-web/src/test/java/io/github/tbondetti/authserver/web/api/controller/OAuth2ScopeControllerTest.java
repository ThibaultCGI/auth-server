package io.github.tbondetti.authserver.web.api.controller;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.web.api.dto.CreateOAuth2ScopeRequest;
import io.github.tbondetti.authserver.web.api.mapper.OAuth2ScopeWebMapper;
import io.github.tbondetti.authserver.web.api.response.OAuth2ScopeResponse;
import io.github.tbondetti.authserver.web.facade.OAuth2ScopeFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.tbondetti.authserver.web.api.mapper.OAuth2ScopeWebMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ScopeControllerTest {

    @InjectMocks
    private OAuth2ScopeController subject;

    @Mock
    private OAuth2ScopeFacade oauth2ScopeFacade;


    @Test
    void getOAuth2ScopeOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";

        final OAuth2Scope scope = OAuth2Scope.builder().build();

        when(this.oauth2ScopeFacade.getOAuth2Scope(applicationCode, code)).thenReturn(scope);

        try (final MockedStatic<OAuth2ScopeWebMapper> utilities = mockStatic(OAuth2ScopeWebMapper.class)) {
            final OAuth2ScopeResponse expected = OAuth2ScopeResponse.builder().build();
            utilities.when(() -> toResponse(scope)).thenReturn(expected);

            assertSame(expected, this.subject.getOAuth2Scope(applicationCode, code));
        }
    }

    @Test
    void createOAuth2ScopeOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final CreateOAuth2ScopeRequest request = new CreateOAuth2ScopeRequest(
                applicationCode,
                code,
                name,
                description
        );

        final OAuth2Scope scope = OAuth2Scope.builder().build();

        when(this.oauth2ScopeFacade.createOAuth2Scope(
                applicationCode,
                code,
                name,
                description
        )).thenReturn(scope);

        try (final MockedStatic<OAuth2ScopeWebMapper> utilities = mockStatic(OAuth2ScopeWebMapper.class)) {
            final OAuth2ScopeResponse expected = OAuth2ScopeResponse.builder().build();

            utilities.when(() -> toResponse(scope)).thenReturn(expected);

            assertSame(expected, this.subject.createOAuth2Scope(request));
        }
    }
}