package io.github.tbondetti.authserver.core.usecase.user;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllUserRolesUseCaseTest {

    @InjectMocks
    private GetAllUserRolesUseCase subject;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private GetUserUseCase getUserUseCase;

    @Test
    void executeOk() {
        final String givenUsername = "givenUsername";

        final String username = "username";
        final User user = User.builder().username(username).build();
        when(this.getUserUseCase.execute(givenUsername)).thenReturn(user);

        final Role role1 = Role.builder().code("code1").build();
        final Role role2 = Role.builder().code("code2").build();
        final List<Role> roles = List.of(role1, role2);
        when(this.userRepositoryPort.getAllUserRoles(username)).thenReturn(roles);

        assertSame(roles, this.subject.execute(givenUsername));
    }

}
