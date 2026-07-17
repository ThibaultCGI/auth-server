package io.github.tbondetti.authserver.core.usecase.user;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignRoleToUserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final GetUserUseCase getUserUseCase;
    private final GetApplicationUseCase getApplicationUseCase;
    private final GetRoleUseCase getRoleUseCase;

    public void execute(
            final String username,
            final String roleCode,
            final String applicationCode
    ) {
        final User user = this.getUserUseCase.execute(username);
        final Application application = this.getApplicationUseCase.execute(applicationCode);
        final Role role = this.getRoleUseCase.execute(roleCode, applicationCode);

        this.userRepositoryPort.addRoleToUser(user, application, role);
    }
}
