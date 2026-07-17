package io.github.tbondetti.authserver.core.port;

import io.github.tbondetti.authserver.core.domain.Role;

import java.util.Optional;

public interface RoleRepositoryPort {

    Optional<Role> findByCodeAndApplicationCode(
            final String code,
            final String applicationCode
    );

    Role save(final Role role);
}
