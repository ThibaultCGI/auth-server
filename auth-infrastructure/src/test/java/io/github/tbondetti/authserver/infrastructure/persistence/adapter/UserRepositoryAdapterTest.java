package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.constants.TestConstants.USER_NAME;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @InjectMocks
    private UserRepositoryAdapter subject;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Test
    void findByUsernameOk() {
        final UserEntity userEntity = new UserEntity();
        final User expected = User.builder().build();

        when(this.userJpaRepository.findByUsername(USER_NAME)).thenReturn(Optional.of(userEntity));
        try (final MockedStatic<UserMapper> utilities = mockStatic(UserMapper.class)) {
            utilities.when(() -> toDomain(userEntity)).thenReturn(expected); // déjà testé

            assertEquals(Optional.of(expected), this.subject.findByUsername(USER_NAME));
        }
    }

    @Test
    void saveOk() {
        final User givenUser = User.builder().build();

        final UserEntity mappedEntity = new UserEntity();
        final UserEntity savedEntity = new UserEntity();

        final User expected = User.builder().build();

        try (final MockedStatic<UserMapper> utilities = mockStatic(UserMapper.class)) {
            utilities.when(() -> toEntity(givenUser)).thenReturn(mappedEntity); // déjà testé

            when(this.userJpaRepository.save(mappedEntity)).thenReturn(savedEntity);

            utilities.when(() -> toDomain(savedEntity)).thenReturn(expected); // déjà testé

            assertSame(expected, this.subject.save(givenUser));
        }
    }
}