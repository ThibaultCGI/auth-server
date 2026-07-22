package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.infrastructure.web.response.UserResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.UserWebMapper.toResponse;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

class UserWebMapperTest {

    @Test
    void toResponseOk() {
        final UUID id = randomUUID();
        final String username = "username";
        final boolean enabled = true;

        final User user = User.builder()
                .id(id)
                .username(username)
                .enabled(enabled)
                .build();

        final UserResponse expected = UserResponse.builder()
                .id(id)
                .username(username)
                .enabled(enabled)
                .build();

        final UserResponse actual = toResponse(user);
        assertEquals(expected, actual);
    }
}