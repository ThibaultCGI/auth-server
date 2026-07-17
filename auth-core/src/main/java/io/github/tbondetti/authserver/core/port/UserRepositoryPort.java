package io.github.tbondetti.authserver.core.port;

import io.github.tbondetti.authserver.core.domain.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByUsername(final String userName);

    User save(final User user);
}
