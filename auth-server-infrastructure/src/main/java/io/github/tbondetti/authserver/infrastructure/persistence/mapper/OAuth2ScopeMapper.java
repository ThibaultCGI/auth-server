package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ScopeEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ScopeMapper {

    public static OAuth2Scope toDomain(final OAuth2ScopeEntity entity) {
        return OAuth2Scope.builder()
                .id(entity.getId())
                .applicationCode(entity.getApplication().getCode())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public static OAuth2ScopeEntity toEntity(
            final OAuth2Scope domain,
            final ApplicationEntity application
    ) {
        return new OAuth2ScopeEntity(
                domain.id(),
                domain.code(),
                domain.name(),
                domain.description(),
                application
        );
    }
}
