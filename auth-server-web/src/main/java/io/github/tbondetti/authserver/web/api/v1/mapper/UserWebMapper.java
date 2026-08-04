package io.github.tbondetti.authserver.web.api.v1.mapper;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.web.api.v1.response.UserResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserWebMapper {

    public static UserResponse toResponse(final User user) {
        return UserResponse.builder()
                .username(user.username())
                .enabled(user.enabled())
                .build();
    }
}
