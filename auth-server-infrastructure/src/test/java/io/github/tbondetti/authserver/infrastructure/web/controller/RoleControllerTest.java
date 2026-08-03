package io.github.tbondetti.authserver.infrastructure.web.controller;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.infrastructure.service.RoleService;
import io.github.tbondetti.authserver.infrastructure.web.dto.CreateRoleRequest;
import io.github.tbondetti.authserver.infrastructure.web.mapper.RoleWebMapper;
import io.github.tbondetti.authserver.infrastructure.web.response.RoleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.RoleWebMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @InjectMocks
    private RoleController subject;

    @Mock
    private RoleService roleService;

    @Test
    void getRoleOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";

        final Role role = Role.builder().build();
        when(this.roleService.getRole(applicationCode, code)).thenReturn(role);

        final RoleResponse roleResponse = RoleResponse.builder().build();
        try (final MockedStatic<RoleWebMapper> userUtilities = mockStatic(RoleWebMapper.class)) {
            userUtilities.when(() -> toResponse(role)).thenReturn(roleResponse); // déjà testé

            assertSame(roleResponse, this.subject.getRole(applicationCode, code));
        }
    }

    @Test
    void createRoleOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final CreateRoleRequest createRoleRequest = new CreateRoleRequest(applicationCode, code, name, description);

        final Role role = Role.builder().build();
        when(this.roleService.createRole(applicationCode, code, name, description)).thenReturn(role);

        final RoleResponse roleResponse = RoleResponse.builder().build();
        try (final MockedStatic<RoleWebMapper> userUtilities = mockStatic(RoleWebMapper.class)) {
            userUtilities.when(() -> toResponse(role)).thenReturn(roleResponse); // déjà testé

            assertSame(roleResponse, this.subject.createRole(createRoleRequest));
        }
    }

    @Test
    void deleteRoleOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";

        doNothing().when(this.roleService).deleteRole(applicationCode, code);

        this.subject.deleteRole(applicationCode, code);

        verify(this.roleService, times(1)).deleteRole(applicationCode, code);
    }

}