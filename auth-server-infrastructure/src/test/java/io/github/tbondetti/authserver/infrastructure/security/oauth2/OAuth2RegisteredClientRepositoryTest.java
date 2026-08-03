package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ScopeEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ScopeMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.OAuth2ScopeJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ScopeMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.security.oauth2.OAuth2RegisteredClientMapper.toRegisteredClient;
import static io.github.tbondetti.authserver.infrastructure.security.oauth2.OAuth2RegisteredClientRepository.ERROR_DO_NOT_SAVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

@ExtendWith(MockitoExtension.class)
class OAuth2RegisteredClientRepositoryTest {

    static final RegisteredClient REGISTERED_CLIENT = RegisteredClient.withId("idRegisteredClient")
            .clientId("clientIdRegisteredClient")
            .authorizationGrantType(CLIENT_CREDENTIALS)
            .build();

    @InjectMocks
    private OAuth2RegisteredClientRepository subject;

    @Mock
    private OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;

    @Mock
    private OAuth2ScopeJpaRepository oauth2ScopeJpaRepository;


    @Test
    void saveOK() {
        final UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> this.subject.save(REGISTERED_CLIENT)
        );

        assertEquals(ERROR_DO_NOT_SAVE, exception.getMessage());
    }

    @Test
    void findByIdOk() {
        final String id = UUID.randomUUID().toString();
        final UUID uuid = UUID.fromString(id);

        final OAuth2Client oAuth2Client = OAuth2Client.builder().build();
        when(this.oauth2ClientRepositoryPort.findById(uuid)).thenReturn(Optional.of(oAuth2Client));

        final OAuth2ScopeEntity scopeEntity = new OAuth2ScopeEntity();

        when(this.oauth2ScopeJpaRepository.findAllByClientId(oAuth2Client.clientId()))
                .thenReturn(List.of(scopeEntity));

        try (MockedStatic<OAuth2ScopeMapper> scopeMapperUtilities = mockStatic(OAuth2ScopeMapper.class, CALLS_REAL_METHODS);
             MockedStatic<OAuth2RegisteredClientMapper> utilities = mockStatic(OAuth2RegisteredClientMapper.class, CALLS_REAL_METHODS)
        ) {
            final OAuth2Scope scope = OAuth2Scope.builder().build();
            scopeMapperUtilities.when(() -> toDomain(scopeEntity)).thenReturn(scope);

            utilities.when(() -> toRegisteredClient(
                    oAuth2Client,
                    List.of(scope)
            )).thenReturn(REGISTERED_CLIENT);

            assertSame(REGISTERED_CLIENT, this.subject.findById(id));
        }
    }

    @Test
    void findByIdOk2() {
        final String id = UUID.randomUUID().toString();
        final UUID uuid = UUID.fromString(id);

        when(this.oauth2ClientRepositoryPort.findById(uuid)).thenReturn(Optional.empty());

        assertNull(this.subject.findById(id));
    }

    @Test
    void findByClientId() {
        final String clientId = "clientId";

        final OAuth2Client oAuth2Client = OAuth2Client.builder()
                .clientId(clientId)
                .build();

        when(this.oauth2ClientRepositoryPort.findByClientId(clientId)).thenReturn(Optional.of(oAuth2Client));

        final OAuth2ScopeEntity scopeEntity = new OAuth2ScopeEntity();

        when(this.oauth2ScopeJpaRepository.findAllByClientId(clientId)).thenReturn(List.of(scopeEntity));

        try (MockedStatic<OAuth2ScopeMapper> scopeMapperUtilities = mockStatic(OAuth2ScopeMapper.class, CALLS_REAL_METHODS);
             MockedStatic<OAuth2RegisteredClientMapper> utilities = mockStatic(OAuth2RegisteredClientMapper.class, CALLS_REAL_METHODS)
        ) {
            final OAuth2Scope scope = OAuth2Scope.builder().build();
            scopeMapperUtilities.when(() -> toDomain(scopeEntity)).thenReturn(scope);

            utilities.when(() -> toRegisteredClient(
                    oAuth2Client,
                    List.of(scope)
            )).thenReturn(REGISTERED_CLIENT);

            assertSame(REGISTERED_CLIENT, this.subject.findByClientId(clientId));
        }
    }

    @Test
    void findByClientId2() {
        final String clientId = "clientId";

        when(this.oauth2ClientRepositoryPort.findByClientId(clientId)).thenReturn(Optional.empty());

        assertNull(this.subject.findByClientId(clientId));
    }
}