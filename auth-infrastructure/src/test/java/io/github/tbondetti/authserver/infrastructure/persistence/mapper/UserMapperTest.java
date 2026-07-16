package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toEntity;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toDomainOk() {
        final UUID id = randomUUID();
        final String username = "username";
        final String passwordHash = "passwordHash";
        final boolean enabled = true;
        final LocalDateTime createdAt = LocalDateTime.now();

        final UserEntity userEntity = new UserEntity(
                id,
                username,
                passwordHash,
                enabled,
                createdAt
        );

        final User expected = User.builder()
                .id(id)
                .username(username)
                .passwordHash(passwordHash)
                .enabled(enabled)
                .createdAt(createdAt)
                .build();

        assertEquals(expected, toDomain(userEntity));
    }

    @Test
    void toEntityOk() {
        final UUID id = randomUUID();
        final String username = "username";
        final String passwordHash = "passwordHash";
        final boolean enabled = true;
        final LocalDateTime createdAt = LocalDateTime.now();

        final User user = User.builder()
                .id(id)
                .username(username)
                .passwordHash(passwordHash)
                .enabled(enabled)
                .createdAt(createdAt)
                .build();

        final UserEntity actual = toEntity(user);

        assertSame(id, actual.getId());
        assertSame(username, actual.getUsername());
        assertSame(passwordHash, actual.getPasswordHash());
        assertSame(enabled, actual.isEnabled());
        assertSame(createdAt, actual.getCreatedAt());
    }
}