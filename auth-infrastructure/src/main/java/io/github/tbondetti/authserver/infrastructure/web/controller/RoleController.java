package io.github.tbondetti.authserver.infrastructure.web.controller;

import io.github.tbondetti.authserver.infrastructure.service.RoleService;
import io.github.tbondetti.authserver.infrastructure.web.dto.CreateRoleRequest;
import io.github.tbondetti.authserver.infrastructure.web.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.RoleWebMapper.toResponse;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse createRole(@RequestBody final CreateRoleRequest request) {
        return toResponse(this.roleService.createRole(
                request.codeApplication(),
                request.code(),
                request.name(),
                request.description()
        ));
    }

    @GetMapping("/{applicationCode}/{code}")
    public RoleResponse getRole(
            @PathVariable final String applicationCode,
            @PathVariable final String code
    ) {
        return toResponse(this.roleService.getRole(applicationCode, code));
    }

    @DeleteMapping("/{applicationCode}/{code}")
    public void deleteRole(
            @PathVariable final String applicationCode,
            @PathVariable final String code
    ) {
        this.roleService.deleteRole(applicationCode, code);
    }
}
