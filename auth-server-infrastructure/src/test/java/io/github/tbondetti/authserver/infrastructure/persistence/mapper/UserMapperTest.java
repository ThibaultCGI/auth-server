package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toEntity;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class UserMapperTest {

    @Test
    void toDomainOk() {
        final UUID id = randomUUID();
        final String username = "username";
        final String passwordHash = "passwordHash";
        final boolean enabled = true;

        final UserEntity userEntity = new UserEntity(
                id,
                username,
                passwordHash,
                enabled
        );

        final User expected = User.builder()
                .id(id)
                .username(username)
                .passwordHash(passwordHash)
                .enabled(enabled)
                .build();

        assertEquals(expected, toDomain(userEntity));
    }

    @Test
    void toEntityOk() {
        final UUID id = randomUUID();
        final String username = "username";
        final String passwordHash = "passwordHash";
        final boolean enabled = true;

        final User user = User.builder()
                .id(id)
                .username(username)
                .passwordHash(passwordHash)
                .enabled(enabled)
                .build();

        final UserEntity actual = toEntity(user);

        assertSame(id, actual.getId());
        assertSame(username, actual.getUsername());
        assertSame(passwordHash, actual.getPasswordHash());
        assertSame(enabled, actual.isEnabled());
    }
}