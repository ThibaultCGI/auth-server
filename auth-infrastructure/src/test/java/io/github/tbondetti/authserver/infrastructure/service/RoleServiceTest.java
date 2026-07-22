package io.github.tbondetti.authserver.infrastructure.service;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.usecase.role.CreateRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.role.DeleteRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService subject;

    @Mock
    private CreateRoleUseCase createRoleUseCase;

    @Mock
    private GetRoleUseCase getRoleUseCase;

    @Mock
    private DeleteRoleUseCase deleteRoleUseCase;

    @Test
    void getRoleOk() {
        final String givenApplicationCode = "givenApplicationCode";
        final String givenCode = "givenCode";
        final Role role = Role.builder().build();
        when(this.getRoleUseCase.execute(givenApplicationCode, givenCode)).thenReturn(role);
        assertSame(role, this.subject.getRole(givenApplicationCode, givenCode));
    }

    @Test
    void createRoleOk() {
        final String codeApplication = "codeApplication";
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final Role role = Role.builder().build();
        when(this.createRoleUseCase.execute(codeApplication, code, name, description)).thenReturn(role);

        assertSame(role, this.subject.createRole(codeApplication, code, name, description));
    }

    @Test
    void deleteRoleOk() {
        final String codeApplication = "codeApplication";
        final String code = "code";

        doNothing().when(this.deleteRoleUseCase).execute(codeApplication, code);

        this.subject.deleteRole(codeApplication, code);

        verify(this.deleteRoleUseCase, times(1)).execute(codeApplication, code);
    }

}