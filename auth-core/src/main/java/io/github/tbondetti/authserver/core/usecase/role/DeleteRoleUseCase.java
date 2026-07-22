package io.github.tbondetti.authserver.core.usecase.role;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;
    private final GetRoleUseCase getRoleUseCase;

    public void execute(
            final String applicationCode,
            final String code

    ) {
        final Role role = this.getRoleUseCase.execute(applicationCode, code);

        this.roleRepositoryPort.delete(role);
    }

}
