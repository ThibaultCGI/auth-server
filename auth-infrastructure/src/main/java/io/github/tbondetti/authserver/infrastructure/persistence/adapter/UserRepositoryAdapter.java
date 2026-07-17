package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toEntity;

@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByUsername(final String username) {
        return this.userJpaRepository.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public User save(final User user) {
        final UserEntity newUser = toEntity(user);
        return toDomain(this.userJpaRepository.save(newUser));
    }
}
