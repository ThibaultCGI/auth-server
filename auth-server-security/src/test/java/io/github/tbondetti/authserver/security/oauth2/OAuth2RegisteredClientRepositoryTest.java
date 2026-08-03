package io.github.tbondetti.authserver.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.core.port.OAuth2ScopeRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.tbondetti.authserver.security.oauth2.OAuth2RegisteredClientMapper.toRegisteredClient;
import static io.github.tbondetti.authserver.security.oauth2.OAuth2RegisteredClientRepository.ERROR_DO_NOT_SAVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

@ExtendWith(MockitoExtension.class)
class OAuth2RegisteredClientRepositoryTest {

    static final RegisteredClient REGISTERED_CLIENT = RegisteredClient.withId("idRegisteredClient")
            .clientId("clientIdRegisteredClient")
            .authorizationGrantType(CLIENT_CREDENTIALS)
            .build();

    @Spy
    @InjectMocks
    private OAuth2RegisteredClientRepository subject;

    @Mock
    private OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;

    @Mock
    private OAuth2ScopeRepositoryPort oauth2ScopeRepositoryPort;

    @Test
    void toClientOk() {
        final String clientId = "clientId";
        final OAuth2Client client = OAuth2Client.builder().clientId(clientId).build();

        final UUID uuid = UUID.randomUUID();
        final OAuth2Scope scope = OAuth2Scope.builder().id(uuid).build();
        final List<OAuth2Scope> scopes = List.of(scope);

        when(this.oauth2ScopeRepositoryPort.findAllByClientId(clientId)).thenReturn(scopes);
        try (final MockedStatic<OAuth2RegisteredClientMapper> utilities = mockStatic(OAuth2RegisteredClientMapper.class)) {
            utilities.when(() -> toRegisteredClient(client, scopes)).thenReturn(REGISTERED_CLIENT);

            assertSame(REGISTERED_CLIENT, this.subject.toClient(client));
        }
    }

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

        final OAuth2Client client = OAuth2Client.builder().id(uuid).build();
        when(this.oauth2ClientRepositoryPort.findById(uuid)).thenReturn(Optional.of(client));

        doReturn(REGISTERED_CLIENT).when(this.subject).toClient(client); // déjà testé

        assertSame(REGISTERED_CLIENT, this.subject.findById(id));
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

        final OAuth2Client client = OAuth2Client.builder().clientId(clientId).build();

        when(this.oauth2ClientRepositoryPort.findByClientId(clientId)).thenReturn(Optional.of(client));

        doReturn(REGISTERED_CLIENT).when(this.subject).toClient(client); // déjà testé

        assertSame(REGISTERED_CLIENT, this.subject.findByClientId(clientId));
    }

    @Test
    void findByClientId2() {
        final String clientId = "clientId";

        when(this.oauth2ClientRepositoryPort.findByClientId(clientId)).thenReturn(Optional.empty());

        assertNull(this.subject.findByClientId(clientId));
    }
}