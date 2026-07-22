package io.github.tbondetti.authserver.infrastructure.config.usecase;

import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.role.CreateRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.role.DeleteRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RoleUseCaseConfigurationTest {

    @InjectMocks
    private RoleUseCaseConfiguration subject;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;

    @Mock
    private GetApplicationUseCase getApplicationUseCase;

    @Mock
    private GetRoleUseCase getRoleUseCase;

    @Test
    void createRoleUseCaseShouldReturnCreateRoleUseCase() {
        final CreateRoleUseCase actual = this.subject.createRoleUseCase(
                this.roleRepositoryPort,
                this.getApplicationUseCase
        );

        assertNotNull(actual);
        assertInstanceOf(CreateRoleUseCase.class, actual);
    }

    @Test
    void getRoleUseCaseShouldReturnGetRoleUseCase() {
        final GetRoleUseCase actual = this.subject.getRoleUseCase(
                this.roleRepositoryPort,
                this.getApplicationUseCase
        );

        assertNotNull(actual);
        assertInstanceOf(GetRoleUseCase.class, actual);
    }

    @Test
    void deleteRoleUseCaseShouldReturnDeleteRoleUseCase() {
        final DeleteRoleUseCase actual = this.subject.deleteRoleUseCase(
                this.roleRepositoryPort,
                this.getRoleUseCase
        );

        assertNotNull(actual);
        assertInstanceOf(DeleteRoleUseCase.class, actual);
    }
}