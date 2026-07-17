package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toDomain(final UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .passwordHash(userEntity.getPasswordHash())
                .enabled(userEntity.isEnabled())
                .build();
    }

    public static UserEntity toEntity(final User user) {
        return new UserEntity(
                user.id(),
                user.username(),
                user.passwordHash(),
                user.enabled()
        );
    }
}
