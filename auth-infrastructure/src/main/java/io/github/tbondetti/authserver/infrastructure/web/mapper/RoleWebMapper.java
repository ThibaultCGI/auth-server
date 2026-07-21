package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.infrastructure.web.response.RoleResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleWebMapper {

    public static RoleResponse toResponse(final Role role) {
        return RoleResponse
                .builder()
                .codeApplication(role.codeApplication())
                .id(role.id())
                .code(role.code())
                .name(role.name())
                .description(role.description())
                .build();
    }
}
