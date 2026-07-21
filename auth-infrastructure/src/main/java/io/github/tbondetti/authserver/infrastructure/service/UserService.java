package io.github.tbondetti.authserver.infrastructure.service;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.usecase.user.AssignRoleToUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.CreateUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final GetUserUseCase getUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final AssignRoleToUserUseCase assignRoleToUserUseCase;

    public User getUser(final String username) {
        return this.getUserUseCase.execute(username);
    }

    @Transactional
    public User createUser(
            final String username,
            final String password
    ) {
        return this.createUserUseCase.execute(
                username,
                password
        );
    }

    @Transactional
    public void assignUserRole(
            final String username,
            final String applicationCode,
            final String roleCode
    ) {
        this.assignRoleToUserUseCase.execute(
                username,
                applicationCode,
                roleCode
        );
    }
}
