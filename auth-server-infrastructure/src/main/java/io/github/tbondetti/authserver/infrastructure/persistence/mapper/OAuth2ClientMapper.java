package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ClientEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ClientMapper {

    public static OAuth2Client toDomain(final OAuth2ClientEntity entity) {
        return OAuth2Client.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .clientName(entity.getClientName())
                .clientSecretHash(entity.getClientSecret())
                .applicationCode(entity.getApplication().getCode())
                .build();
    }

    public static OAuth2ClientEntity toEntity(
            final OAuth2Client domain,
            final ApplicationEntity application
    ) {
        return new OAuth2ClientEntity(
                domain.id(),
                domain.clientId(),
                domain.clientName(),
                domain.clientSecretHash(),
                application
        );
    }
}
