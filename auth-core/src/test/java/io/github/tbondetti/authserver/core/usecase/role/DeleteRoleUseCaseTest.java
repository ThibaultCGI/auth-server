package io.github.tbondetti.authserver.core.usecase.role;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteRoleUseCaseTest {

    @InjectMocks
    private DeleteRoleUseCase subject;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;

    @Mock
    private GetRoleUseCase getRoleUseCase;

    @Test
    void executeOk() {
        final String givenApplicationCode = "givenApplicationCode";
        final String givenCode = "givenCode";

        final Role role = Role.builder().build();
        when(this.getRoleUseCase.execute(givenApplicationCode, givenCode)).thenReturn(role);

        doNothing().when(this.roleRepositoryPort).delete(role);

        this.subject.execute(givenApplicationCode, givenCode);

        verify(this.roleRepositoryPort, times(1)).delete(role);
    }
}