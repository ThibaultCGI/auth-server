package io.github.tbondetti.authserver.web.api.controller;

import io.github.tbondetti.authserver.web.api.dto.CreateRoleRequest;
import io.github.tbondetti.authserver.web.api.response.RoleResponse;
import io.github.tbondetti.authserver.web.facade.RoleFacade;
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

import static io.github.tbondetti.authserver.web.api.mapper.RoleWebMapper.toResponse;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleFacade roleFacade;

    @GetMapping("/{applicationCode}/{code}")
    public RoleResponse getRole(
            @PathVariable final String applicationCode,
            @PathVariable final String code
    ) {
        return toResponse(this.roleFacade.getRole(applicationCode, code));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse createRole(@RequestBody final CreateRoleRequest request) {
        return toResponse(this.roleFacade.createRole(
                request.codeApplication(),
                request.code(),
                request.name(),
                request.description()
        ));
    }

    @DeleteMapping("/{applicationCode}/{code}")
    public void deleteRole(
            @PathVariable final String applicationCode,
            @PathVariable final String code
    ) {
        this.roleFacade.deleteRole(applicationCode, code);
    }
}
