package io.github.tbondetti.authserver.core.usecase.user;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetAllUserRolesUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final GetUserUseCase getUserUseCase;

    public List<Role> execute(final String username) {
        final User user = this.getUserUseCase.execute(username);

        return this.userRepositoryPort.getAllUserRoles(user.username());
    }
}
