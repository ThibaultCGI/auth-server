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
            final String roleCode,
            final String applicationCode
    ) {
        final User user = this.getUserUseCase.execute(username);
        final Application application = this.getApplicationUseCase.execute(applicationCode);
        final Role role = this.getRoleUseCase.execute(roleCode, applicationCode);

        this.ensureRoleIsNotAlreadyAsigned(user, application, role);

        this.userRepositoryPort.addRoleToUser(user.username(), application.code(), role.code());
    }

    void ensureRoleIsNotAlreadyAsigned(
            final User user,
            final Application application,
            final Role role
    ) {

        final Optional<Role> optionalRole = this.roleRepositoryPort.findByCodeAndApplicationCodeAndUsername(
                role.code(),
                application.code(),
                user.username()
        );

        if (optionalRole.isPresent()) {
            throw new AuthServerFunctionalException(ERROR_ROLE_ALREADY_ASSIGNED.formatted(
                    role.code(),
                    application.code(),
                    user.username()
            ));
        }

    }
}
