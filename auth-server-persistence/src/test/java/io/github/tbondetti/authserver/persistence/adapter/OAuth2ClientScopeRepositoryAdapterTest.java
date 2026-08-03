package io.github.tbondetti.authserver.persistence.adapter;

import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientScopeEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientScopeId;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ScopeEntity;
import io.github.tbondetti.authserver.persistence.repository.OAuth2ClientJpaRepository;
import io.github.tbondetti.authserver.persistence.repository.OAuth2ClientScopeJpaRepository;
import io.github.tbondetti.authserver.persistence.repository.OAuth2ScopeJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ClientScopeRepositoryAdapterTest {

    @InjectMocks
    private OAuth2ClientScopeRepositoryAdapter subject;

    @Mock
    private OAuth2ClientScopeJpaRepository oauth2ClientScopeJpaRepository;

    @Mock
    private OAuth2ClientJpaRepository oauth2ClientJpaRepository;

    @Mock
    private OAuth2ScopeJpaRepository oauth2ScopeJpaRepository;


    @Test
    void existsOkWhenAssociationExists() {
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String clientId = "clientId";

        final OAuth2ClientScopeEntity entity = new OAuth2ClientScopeEntity();

        when(this.oauth2ClientScopeJpaRepository.findByApplicationCodeAndCodeAndClientId(
                applicationCode,
                code,
                clientId
        )).thenReturn(Optional.of(entity));

        assertTrue(this.subject.exists(
                applicationCode,
                code,
                clientId
        ));
    }

    @Test
    void existsOkWhenAssociationDoesNotExist() {
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String clientId = "clientId";

        when(this.oauth2ClientScopeJpaRepository.findByApplicationCodeAndCodeAndClientId(
                applicationCode,
                code,
                clientId
        )).thenReturn(Optional.empty());

        assertFalse(this.subject.exists(
                applicationCode,
                code,
                clientId
        ));
    }

    @Test
    void assignOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String clientId = "clientId";

        final UUID scopeId = UUID.randomUUID();
        final OAuth2ScopeEntity scope = new OAuth2ScopeEntity();
        scope.setId(scopeId);

        when(this.oauth2ScopeJpaRepository.getByApplicationCodeAndCode(
                applicationCode,
                code
        )).thenReturn(scope);

        final UUID clientUuid = UUID.randomUUID();
        final OAuth2ClientEntity client = new OAuth2ClientEntity();
        client.setId(clientUuid);

        when(this.oauth2ClientJpaRepository.getByClientId(clientId)).thenReturn(client);

        this.subject.assign(applicationCode, code, clientId);

        verify(this.oauth2ClientScopeJpaRepository, times(1)).save(
                argThat(entity -> {
                    final OAuth2ClientScopeId id = entity.getId();

                    return new OAuth2ClientScopeId(clientUuid, scopeId).equals(id)
                            && entity.getClient() == client
                            && entity.getScope() == scope;
                })
        );
    }
}