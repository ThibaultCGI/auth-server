package io.github.tbondetti.authserver.web.api.v1.mapper;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.web.api.v1.response.RoleResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleWebMapper {

    public static RoleResponse toResponse(final Role role) {
        return RoleResponse
                .builder()
                .codeApplication(role.codeApplication())
                .code(role.code())
                .name(role.name())
                .description(role.description())
                .build();
    }
}
