package io.github.tbondetti.authserver.core.usecase.user;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.usecase.user.AssignRoleToUserUseCase.ERROR_ROLE_ALREADY_ASSIGNED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignRoleToUserUseCaseTest {

    @Spy
    @InjectMocks
    private AssignRoleToUserUseCase subject;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private GetUserUseCase getUserUseCase;

    @Mock
    private GetApplicationUseCase getApplicationUseCase;

    @Mock
    private GetRoleUseCase getRoleUseCase;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;

    @Test
    void ensureRoleIsNotAlreadyAsignedKo() {
        final String givenUsername = "givenUsername";
        final String givenApplicationCode = "givenCodeApplication";
        final String givenRoleCode = "givenRoleCode";

        final Role foundRole = Role.builder().build();
        when(this.roleRepositoryPort.findByCodeAndApplicationCodeAndUsername(
                givenRoleCode,
                givenApplicationCode,
                givenUsername
        )).thenReturn(Optional.of(foundRole));

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> this.subject.ensureRoleIsNotAlreadyAsigned(
                        givenUsername,
                        givenApplicationCode,
                        givenRoleCode
                )
        );
        assertEquals(ERROR_ROLE_ALREADY_ASSIGNED.formatted(
                givenRoleCode,
                givenUsername,
                givenApplicationCode
        ), exception.getMessage());
    }

    @Test
    void ensureRoleIsNotAlreadyAsignedOk() {
        final String givenUsername = "givenUsername";
        final String givenApplicationCode = "givenCodeApplication";
        final String givenRoleCode = "givenRoleCode";

        when(this.roleRepositoryPort.findByCodeAndApplicationCodeAndUsername(
                givenRoleCode,
                givenApplicationCode,
                givenUsername
        )).thenReturn(Optional.empty());

        this.subject.ensureRoleIsNotAlreadyAsigned(
                givenUsername,
                givenApplicationCode,
                givenRoleCode
        );

        verify(this.roleRepositoryPort, times(1)).findByCodeAndApplicationCodeAndUsername(
                givenRoleCode,
                givenApplicationCode,
                givenUsername
        );
    }

    @Test
    void executeOk() {
        final String givenUsername = "givenUsername";
        final String givenApplicationCode = "givenApplicationCode";
        final String givenRoleCode = "givenRoleCode";

        final String username = "username";
        final User user = User.builder().username(username).build();
        when(this.getUserUseCase.execute(givenUsername)).thenReturn(user);

        final String codeApplication = "codeApplication";
        final Application application = Application.builder().code(codeApplication).build();
        when(this.getApplicationUseCase.execute(givenApplicationCode)).thenReturn(application);

        final String codeRole = "codeRole";
        final Role role = Role.builder().code(codeRole).build();
        when(this.getRoleUseCase.execute(givenApplicationCode, givenRoleCode)).thenReturn(role);

        doNothing().when(this.subject).ensureRoleIsNotAlreadyAsigned(username, codeApplication, codeRole); // déjà testé

        doNothing().when(this.userRepositoryPort).addRoleToUser(username, codeApplication, codeRole);

        this.subject.execute(givenUsername, givenApplicationCode, givenRoleCode);

        verify(this.subject, times(1)).ensureRoleIsNotAlreadyAsigned(username, codeApplication, codeRole);
        verify(this.userRepositoryPort, times(1)).addRoleToUser(username, codeApplication, codeRole);
    }
}