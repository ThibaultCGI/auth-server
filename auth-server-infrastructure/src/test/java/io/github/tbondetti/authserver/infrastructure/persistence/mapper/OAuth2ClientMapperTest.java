package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ClientEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ClientMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ClientMapper.toEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class OAuth2ClientMapperTest {

    @Test
    void toDomainOk() {

        final UUID id = UUID.randomUUID();
        final String clientId = "clientId";
        final String clientName = "clientName";
        final String clientSecretHash = "clientSecretHash";
        final OAuth2ClientEntity entity = new OAuth2ClientEntity();
        entity.setId(id);
        entity.setClientId(clientId);
        entity.setClientName(clientName);
        entity.setClientSecret(clientSecretHash);

        final String applicationCode = "applicationCode";
        final ApplicationEntity application = new ApplicationEntity();
        application.setCode(applicationCode);
        entity.setApplication(application);

        final OAuth2Client expected = OAuth2Client.builder()
                .id(id)
                .clientId(clientId)
                .clientName(clientName)
                .clientSecretHash(clientSecretHash)
                .applicationCode(applicationCode)
                .build();

        assertEquals(expected, toDomain(entity));
    }

    @Test
    void toEntityOk() {

        final UUID id = UUID.randomUUID();
        final String clientId = "clientId";
        final String clientName = "clientName";
        final String clientSecretHash = "clientSecretHash";

        final OAuth2Client domain = OAuth2Client.builder()
                .id(id)
                .clientId(clientId)
                .clientName(clientName)
                .clientSecretHash(clientSecretHash)
                .build();

        final ApplicationEntity application = new ApplicationEntity();

        final OAuth2ClientEntity actual = toEntity(domain, application);
        assertSame(id, actual.getId());
        assertSame(clientId, actual.getClientId());
        assertSame(clientName, actual.getClientName());
        assertSame(clientSecretHash, actual.getClientSecret());
        assertSame(application, actual.getApplication());
    }
}