package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.infrastructure.web.response.OAuth2ScopeResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ScopeWebMapper {

    public static OAuth2ScopeResponse toResponse(final OAuth2Scope scope) {
        return OAuth2ScopeResponse.builder()
                .applicationCode(scope.applicationCode())
                .code(scope.code())
                .name(scope.name())
                .description(scope.description())
                .build();
    }
}
