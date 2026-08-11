package io.github.tbondetti.authserver.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientGrantTypeEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientGrantTypeId;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientRedirectUriEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientRedirectUriId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.REFRESH_TOKEN;
import static io.github.tbondetti.authserver.persistence.mapper.OAuth2ClientMapper.toDomain;
import static io.github.tbondetti.authserver.persistence.mapper.OAuth2ClientMapper.toEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;

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

        final OAuth2ClientGrantTypeEntity grantType1 = new OAuth2ClientGrantTypeEntity();
        grantType1.setId(new OAuth2ClientGrantTypeId(id, AUTHORIZATION_CODE));
        final OAuth2ClientGrantTypeEntity grantType2 = new OAuth2ClientGrantTypeEntity();
        grantType2.setId(new OAuth2ClientGrantTypeId(id, REFRESH_TOKEN));
        entity.setGrantTypes(Set.of(grantType1, grantType2));

        final OAuth2ClientRedirectUriEntity redirectUri1 = new OAuth2ClientRedirectUriEntity();
        redirectUri1.setId(new OAuth2ClientRedirectUriId(id, "uri1"));
        final OAuth2ClientRedirectUriEntity redirectUri2 = new OAuth2ClientRedirectUriEntity();
        redirectUri2.setId(new OAuth2ClientRedirectUriId(id, "uri2"));
        entity.setRedirectUris(Set.of(redirectUri1, redirectUri2));

        final URI uri1 = URI.create("test1");
        final URI uri2 = URI.create("test2");

        try(final MockedStatic<UriMapper> utilities = mockStatic(UriMapper.class)) {
            utilities.when(() -> UriMapper.toDomain("uri1")).thenReturn(uri1);
            utilities.when(() -> UriMapper.toDomain("uri2")).thenReturn(uri2);

            final OAuth2Client expected = OAuth2Client.builder()
                    .id(id)
                    .clientId(clientId)
                    .clientName(clientName)
                    .clientSecretHash(clientSecretHash)
                    .applicationCode(applicationCode)
                    .grantTypes(Set.of(AUTHORIZATION_CODE, REFRESH_TOKEN))
                    .redirectUris(Set.of(uri1, uri2))
                    .build();

            assertEquals(expected, toDomain(entity));
        }
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
                .grantTypes(Set.of(REFRESH_TOKEN))
                .redirectUris(Set.of(URI.create("uri")))
                .build();

        final ApplicationEntity application = new ApplicationEntity();

        final OAuth2ClientEntity actual = toEntity(domain, application);
        assertSame(id, actual.getId());
        assertSame(clientId, actual.getClientId());
        assertSame(clientName, actual.getClientName());
        assertSame(clientSecretHash, actual.getClientSecret());
        assertSame(application, actual.getApplication());

        final OAuth2ClientGrantTypeEntity grantType = actual.getGrantTypes().stream().toList().getFirst();
        assertSame(id, grantType.getId().getIdOauth2Client());
        assertSame(REFRESH_TOKEN, grantType.getId().getGrantType());

        final OAuth2ClientRedirectUriEntity redirectUri = actual.getRedirectUris().stream().toList().getFirst();
        assertSame(id, redirectUri.getId().getIdOauth2Client());
        assertEquals("uri", redirectUri.getId().getRedirectUri());
    }
}