package io.github.tbondetti.authserver.infrastructure.service;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.usecase.user.AssignRoleToUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.CreateUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
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
class UserServiceTest {

    @InjectMocks
    private UserService subject;

    @Mock
    private GetUserUseCase getUserUseCase;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private AssignRoleToUserUseCase assignRoleToUserUseCase;

    @Test
    void getUserOk() {
        final String username = "username";
        final User user = User.builder().build();
        when(this.getUserUseCase.execute(username)).thenReturn(user);
        assertSame(user, this.subject.getUser(username));
    }

    @Test
    void createUserOk() {
        final String username = "username";
        final String password = "password";
        final User user = User.builder().build();

        when(this.createUserUseCase.execute(username, password)).thenReturn(user);
        assertSame(user, this.subject.createUser(username, password));
    }

    @Test
    void assignUserRoleOk() {
        final String username = "username";
        final String applicationCode = "applicationCode";
        final String roleCode = "roleCode";

        doNothing().when(this.assignRoleToUserUseCase).execute(username, applicationCode, roleCode);

        this.subject.assignUserRole(username, applicationCode, roleCode);

        verify(this.assignRoleToUserUseCase, times(1)).execute(username, applicationCode, roleCode);
    }
}