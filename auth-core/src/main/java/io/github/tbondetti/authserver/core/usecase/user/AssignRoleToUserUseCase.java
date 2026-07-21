package io.github.tbondetti.authserver.core.usecase.user;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AssignRoleToUserUseCase {

    static final String ERROR_ROLE_ALREADY_ASSIGNED = "Le rôle %s est déjà attribué à l'utilisateur %s pour l'application %s.";

    private final UserRepositoryPort userRepositoryPort;
    private final GetUserUseCase getUserUseCase;
    private final GetApplicationUseCase getApplicationUseCase;
    private final GetRoleUseCase getRoleUseCase;
    private final RoleRepositoryPort roleRepositoryPort;

    public void execute(
            final String username,
            final String applicationCode,
            final String roleCode
    ) {
        final User user = this.getUserUseCase.execute(username);
        final Application application = this.getApplicationUseCase.execute(applicationCode);
        final Role role = this.getRoleUseCase.execute(applicationCode, roleCode);

        this.ensureRoleIsNotAlreadyAsigned(user.username(), application.code(), role.code());

        this.userRepositoryPort.addRoleToUser(user.username(), application.code(), role.code());
    }

    void ensureRoleIsNotAlreadyAsigned(
            final String username,
            final String applicationCode,
            final String roleCode
    ) {

        final Optional<Role> optionalRole = this.roleRepositoryPort.findByCodeAndApplicationCodeAndUsername(
                roleCode,
                applicationCode,
                username
        );

        if (optionalRole.isPresent()) {
            throw new AuthServerFunctionalException(ERROR_ROLE_ALREADY_ASSIGNED.formatted(
                    roleCode,
                    username,
                    applicationCode
            ));
        }

    }
}
