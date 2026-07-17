package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleMapper {

    public static Role toDomain(final RoleEntity roleEntity) {
        return Role.builder()
                .id(roleEntity.getId())
                .code(roleEntity.getCode())
                .name(roleEntity.getName())
                .description(roleEntity.getDescription())
                .codeApplication(roleEntity.getApplication().getCode())
                .build();
    }

    public static RoleEntity toEntity(
            final Role role,
            final ApplicationEntity applicationEntity
    ) {
        return new RoleEntity(
                role.id(),
                role.code(),
                role.name(),
                role.description(),
                applicationEntity
        );
    }
}
