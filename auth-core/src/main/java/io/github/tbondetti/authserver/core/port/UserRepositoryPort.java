package io.github.tbondetti.authserver.core.port;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByUsername(final String userName);

    User save(final User user);

    void addRoleToUser(final User user,
                       final Application application,
                       final Role role
    );

    List<Role> getUserRolesForApplication(final User user,
                                          final Application application
    );
}
