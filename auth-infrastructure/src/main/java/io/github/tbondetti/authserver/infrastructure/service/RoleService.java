package io.github.tbondetti.authserver.infrastructure.service;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.usecase.role.CreateRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.role.DeleteRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final CreateRoleUseCase createRoleUseCase;
    private final GetRoleUseCase getRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;

    public Role getRole(final String applicationCode,
                        final String code
    ) {
        return this.getRoleUseCase.execute(applicationCode, code);
    }

    @Transactional
    public Role createRole(final String codeApplication,
                           final String code,
                           final String name,
                           final String description
    ) {
        return this.createRoleUseCase.execute(
                codeApplication,
                code,
                name,
                description
        );
    }

    @Transactional
    public void deleteRole(final String applicationCode,
                           final String code
    ) {
        this.deleteRoleUseCase.execute(applicationCode, code);
    }
}
