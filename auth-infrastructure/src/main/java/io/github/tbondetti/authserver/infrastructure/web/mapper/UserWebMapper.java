package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.infrastructure.web.response.UserResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserWebMapper {

    public static UserResponse toResponse(final User user) {
        return UserResponse.builder()
                .id(user.id())
                .username(user.username())
                .enabled(user.enabled())
                .build();
    }
}
