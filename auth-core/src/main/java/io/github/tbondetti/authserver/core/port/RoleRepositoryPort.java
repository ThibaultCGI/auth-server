package io.github.tbondetti.authserver.core.port;

import io.github.tbondetti.authserver.core.domain.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepositoryPort {

    Optional<Role> findById(final UUID id);
    Optional<Role> findByCode(final String code);
}
