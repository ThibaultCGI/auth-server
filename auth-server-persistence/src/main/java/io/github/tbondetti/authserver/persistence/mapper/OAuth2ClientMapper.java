package io.github.tbondetti.authserver.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType;
import io.github.tbondetti.authserver.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientGrantTypeEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientGrantTypeId;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientRedirectUriEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ClientRedirectUriId;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class OAuth2ClientMapper {

    public static OAuth2Client toDomain(final OAuth2ClientEntity entity) {
        final Set<URI> redirectUris = entity.getRedirectUris().stream()
                .map(OAuth2ClientRedirectUriEntity::getId)
                .map(OAuth2ClientRedirectUriId::getRedirectUri)
                .map(UriMapper::toDomain)
                .collect(Collectors.toSet())
                ;

        final Set<OAuth2ClientGrantType> grantTypes = entity.getGrantTypes().stream()
                .map(OAuth2ClientGrantTypeEntity::getId)
                .map(OAuth2ClientGrantTypeId::getGrantType)
                .collect(Collectors.toSet())
                ;

        return OAuth2Client.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .clientName(entity.getClientName())
                .clientSecretHash(entity.getClientSecret())
                .applicationCode(entity.getApplication().getCode())
                .redirectUris(redirectUris)
                .grantTypes(grantTypes)
                .build();
    }

    public static OAuth2ClientEntity toEntity(
            final OAuth2Client domain,
            final ApplicationEntity application
    ) {

        final Set<OAuth2ClientGrantTypeId> grantTypesIds = domain.grantTypes().stream()
                .map(grantType -> new OAuth2ClientGrantTypeId(domain.id(), grantType))
                .collect(Collectors.toSet())
                ;

        final Set<OAuth2ClientRedirectUriId> redirectUriIds = domain.redirectUris().stream()
                .map(uri -> new OAuth2ClientRedirectUriId(domain.id(), UriMapper.toString(uri)))
                .collect(Collectors.toSet())
                ;

        final OAuth2ClientEntity entity = OAuth2ClientEntity.builder()
                .id(domain.id())
                .clientId(domain.clientId())
                .clientName(domain.clientName())
                .clientSecret(domain.clientSecretHash())
                .application(application)
                .build()
                ;

        final Set<OAuth2ClientGrantTypeEntity> grantTypes = grantTypesIds.stream()
                .map(id -> new OAuth2ClientGrantTypeEntity(id, entity))
                .collect(Collectors.toSet())
                ;

        final Set<OAuth2ClientRedirectUriEntity> redirectUris = redirectUriIds.stream()
                        .map(id -> new OAuth2ClientRedirectUriEntity(id, entity))
                        .collect(Collectors.toSet());

        entity.setGrantTypes(grantTypes);
        entity.setRedirectUris(redirectUris);

        return entity;
    }
}
