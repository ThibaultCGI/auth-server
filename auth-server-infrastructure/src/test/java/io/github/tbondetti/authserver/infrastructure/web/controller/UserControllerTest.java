package io.github.tbondetti.authserver.infrastructure.web.controller;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.infrastructure.service.UserService;
import io.github.tbondetti.authserver.infrastructure.web.dto.AssignRoleRequest;
import io.github.tbondetti.authserver.infrastructure.web.dto.CreateUserRequest;
import io.github.tbondetti.authserver.infrastructure.web.mapper.RoleWebMapper;
import io.github.tbondetti.authserver.infrastructure.web.mapper.UserWebMapper;
import io.github.tbondetti.authserver.infrastructure.web.response.RoleResponse;
import io.github.tbondetti.authserver.infrastructure.web.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.UserWebMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController subject;

    @Mock
    private UserService userService;

    @Test
    void getUserOk() {
        final String username = "username";

        final User user = User.builder().build();
        when(this.userService.getUser(username)).thenReturn(user);

        final UserResponse userResponse = UserResponse.builder().build();
        try (final MockedStatic<UserWebMapper> userUtilities = mockStatic(UserWebMapper.class)) {
            userUtilities.when(() -> toResponse(user)).thenReturn(userResponse); // déjà testé

            assertSame(userResponse, this.subject.getUser(username));
        }
    }

    @Test
    void createUserOk() {
        final String username = "username";
        final String password = "password";

        final CreateUserRequest createUserRequest = new CreateUserRequest(username, password);

        final User user = User.builder().build();
        when(this.userService.createUser(username, password)).thenReturn(user);

        final UserResponse userResponse = UserResponse.builder().build();
        try (final MockedStatic<UserWebMapper> userUtilities = mockStatic(UserWebMapper.class)) {
            userUtilities.when(() -> toResponse(user)).thenReturn(userResponse); // déjà testé

            assertSame(userResponse, this.subject.createUser(createUserRequest));
        }
    }

    @Test
    void assignRoleOk() {
        final String username = "username";
        final String applicationCode = "applicationCode";
        final String roleCode = "roleCode";

        final AssignRoleRequest assignRoleRequest = new AssignRoleRequest(applicationCode, roleCode);

        doNothing().when(this.userService).assignUserRole(username, applicationCode, roleCode);

        this.subject.assignRole(username, assignRoleRequest);

        verify(this.userService, times(1)).assignUserRole(username, applicationCode, roleCode);
    }

    @Test
    void getUserRolesOk() {
        final String username = "username";
        final String applicationCode = "applicationCode";

        final Role role1 = Role.builder().code("role1").build();
        final Role role2 = Role.builder().code("role2").build();

        when(this.userService.getUserRoles(username, applicationCode)).thenReturn(List.of(role1, role2));

        try (final MockedStatic<RoleWebMapper> utilities = mockStatic(RoleWebMapper.class)) {
            final RoleResponse roleResponse1 = RoleResponse.builder().build();
            utilities.when(() -> RoleWebMapper.toResponse(role1)).thenReturn(roleResponse1);

            final RoleResponse roleResponse2 = RoleResponse.builder().build();
            utilities.when(() -> RoleWebMapper.toResponse(role2)).thenReturn(roleResponse2);

            assertEquals(List.of(roleResponse1, roleResponse2), this.subject.getUserRoles(username, applicationCode));

        }
    }
}