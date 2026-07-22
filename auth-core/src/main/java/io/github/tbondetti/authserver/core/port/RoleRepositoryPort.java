package io.github.tbondetti.authserver.core.port;

import io.github.tbondetti.authserver.core.domain.Role;

import java.util.Optional;

public interface RoleRepositoryPort {

    Optional<Role> findByApplicationCodeAndCode(
            final String applicationCode,
            final String code
    );

    Role save(final Role role);

    void delete(final Role role);

    Optional<Role> findByCodeAndApplicationCodeAndUsername(
            final String code,
            final String applicationCode,
            final String username
    );
}
