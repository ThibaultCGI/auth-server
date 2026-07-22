package io.github.tbondetti.authserver.infrastructure.config.usecase;

import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.user.AssignRoleToUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.CreateUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetAllUserRolesUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserRolesForApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserUseCaseConfigurationTest {

    @InjectMocks
    private UserUseCaseConfiguration subject;

    @Mock
    private GetUserUseCase getUserUseCase;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private GetApplicationUseCase getApplicationUseCase;

    @Mock
    private GetRoleUseCase getRoleUseCase;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;


    @Test
    void createUserUseCaseShouldReturnCreateUserUseCase() {
        final CreateUserUseCase actual = this.subject.createUserUseCase(
                this.userRepositoryPort,
                this.passwordEncoderPort
        );

        assertNotNull(actual);
        assertInstanceOf(CreateUserUseCase.class, actual);
    }

    @Test
    void getUserUseCaseShouldReturnGetUserUseCase() {
        final GetUserUseCase actual = this.subject.getUserUseCase(this.userRepositoryPort);

        assertNotNull(actual);
        assertInstanceOf(GetUserUseCase.class, actual);
    }

    @Test
    void assignRoleToUserUseCaseShouldReturnAssignRoleToUserUseCase() {
        final AssignRoleToUserUseCase actual = this.subject.assignRoleToUserUseCase(
                this.userRepositoryPort,
                this.getUserUseCase,
                this.getApplicationUseCase,
                this.getRoleUseCase,
                this.roleRepositoryPort
        );

        assertNotNull(actual);
        assertInstanceOf(AssignRoleToUserUseCase.class, actual);
    }

    @Test
    void getUserRolesForApplicationUseCaseShouldReturnGetUserRolesForApplicationUseCase() {
        final GetUserRolesForApplicationUseCase actual = this.subject.getUserRolesForApplicationUseCase(
                this.userRepositoryPort,
                this.getUserUseCase,
                this.getApplicationUseCase
        );

        assertNotNull(actual);
        assertInstanceOf(GetUserRolesForApplicationUseCase.class, actual);
    }

    @Test
    void getAllUserRolesUseCaseShouldReturnGetAllUserRolesUseCase() {
        final GetAllUserRolesUseCase actual = this.subject.getAllUserRolesUseCase(
                this.userRepositoryPort,
                this.getUserUseCase
        );

        assertNotNull(actual);
        assertInstanceOf(GetAllUserRolesUseCase.class, actual);
    }
}