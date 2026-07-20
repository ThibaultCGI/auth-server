package io.github.tbondetti.authserver.core.port;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByUsername(final String userName);

    User save(final User user);

    void addRoleToUser(final String username,
                       final String applicationCode,
                       final String roleCode
    );

    List<Role> getUserRolesForApplication(final String username,
                                          final String applicationCode
    );

    List<Role> getAllUserRoles(final String username);
}
