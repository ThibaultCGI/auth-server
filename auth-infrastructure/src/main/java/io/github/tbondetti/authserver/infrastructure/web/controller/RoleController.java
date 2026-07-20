package io.github.tbondetti.authserver.infrastructure.web.controller;

import io.github.tbondetti.authserver.core.usecase.role.CreateRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import io.github.tbondetti.authserver.infrastructure.web.dto.CreateRoleRequest;
import io.github.tbondetti.authserver.infrastructure.web.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final CreateRoleUseCase createRoleUseCase;
    private final GetRoleUseCase getRoleUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse createRole(@RequestBody final CreateRoleRequest request) {
        return toResponse(this.createRoleUseCase.execute(
                request.code(),
                request.name(),
                request.description(),
                request.codeApplication()
        ));
    }

    @GetMapping("/{applicationCode}.{code}")
    public RoleResponse getApplication(
            @PathVariable final String code,
            @PathVariable final String applicationCode
    ) {
        return toResponse(this.getRoleUseCase.execute(code, applicationCode));
    }
}
