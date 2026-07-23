package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ClientEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ClientMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.OAuth2ClientJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ClientMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ClientMapper.toEntity;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ClientRepositoryAdapterTest {

    @InjectMocks
    private OAuth2ClientRepositoryAdapter subject;

    @Mock
    private OAuth2ClientJpaRepository oauth2ClientJpaRepository;

    @Mock
    private ApplicationJpaRepository applicationJpaRepository;

    @Test
    void findByClientIdOk() {
        final String clientId = "clientId";
        final OAuth2ClientEntity entity = new OAuth2ClientEntity();
        when(this.oauth2ClientJpaRepository.findByClientId(clientId)).thenReturn(Optional.of(entity));

        try (final MockedStatic<OAuth2ClientMapper> utilities = mockStatic(OAuth2ClientMapper.class)) {
            final OAuth2Client client = OAuth2Client.builder().id(randomUUID()).build();
            utilities.when(() -> toDomain(entity)).thenReturn(client); // déjà testé

            assertEquals(Optional.of(client), this.subject.findByClientId(clientId));
        }
    }

    @Test
    void saveOk() {
        final String applicationCode = "applicationCode";
        final OAuth2Client client = OAuth2Client.builder().applicationCode(applicationCode).build();

        final ApplicationEntity application = new ApplicationEntity();
        when(this.applicationJpaRepository.getByCode(applicationCode)).thenReturn(application);

        try (final MockedStatic<OAuth2ClientMapper> utilities = mockStatic(OAuth2ClientMapper.class)) {
            final OAuth2ClientEntity toSave = new OAuth2ClientEntity();
            utilities.when(() -> toEntity(client, application)).thenReturn(toSave); // déjà testé

            final String clientId = "clientId";
            final OAuth2ClientEntity saved = new OAuth2ClientEntity();
            saved.setClientId(clientId);
            when(this.oauth2ClientJpaRepository.save(toSave)).thenReturn(saved);

            final OAuth2ClientEntity found = new OAuth2ClientEntity();

            when(this.oauth2ClientJpaRepository.getByClientId(clientId)).thenReturn(found);

            final OAuth2Client expected = OAuth2Client.builder().id(randomUUID()).build();
            utilities.when(() -> toDomain(found)).thenReturn(expected); // déjà testé

            assertSame(expected, this.subject.save(client));
        }
    }
}
