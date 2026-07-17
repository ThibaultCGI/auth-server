package io.github.tbondetti.authserver.core.usecase.user;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetUserRolesForApplicationUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final GetUserUseCase getUserUseCase;
    private final GetApplicationUseCase getApplicationUseCase;

    public List<Role> execute(
            final String username,
            final String applicationCode
    ) {
        final User user = this.getUserUseCase.execute(username);
        final Application application = this.getApplicationUseCase.execute(applicationCode);

        return this.userRepositoryPort.getUserRolesForApplication(user, application);
    }
}
