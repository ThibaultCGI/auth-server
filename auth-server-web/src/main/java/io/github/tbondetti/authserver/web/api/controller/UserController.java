package io.github.tbondetti.authserver.web.api.controller;

import io.github.tbondetti.authserver.web.api.dto.AssignRoleRequest;
import io.github.tbondetti.authserver.web.api.dto.CreateUserRequest;
import io.github.tbondetti.authserver.web.api.mapper.RoleWebMapper;
import io.github.tbondetti.authserver.web.api.response.RoleResponse;
import io.github.tbondetti.authserver.web.api.response.UserResponse;
import io.github.tbondetti.authserver.web.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.github.tbondetti.authserver.web.api.mapper.UserWebMapper.toResponse;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @GetMapping("/{username}")
    public UserResponse getUser(@PathVariable final String username) {
        return toResponse(this.userFacade.getUser(username));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody final CreateUserRequest request) {
        return toResponse(this.userFacade.createUser(
                request.username(),
                request.password()
        ));
    }

    @GetMapping("/{username}/roles")
    public List<RoleResponse> getUserRoles(
            @PathVariable final String username,
            @RequestParam(
                    value = "applicationCode",
                    required = false
            ) final String applicationCode
    ) {
        return this.userFacade.getUserRoles(username, applicationCode).stream().map(RoleWebMapper::toResponse).toList();
    }

    @PostMapping("/{username}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignRole(
            @PathVariable final String username,
            @RequestBody final AssignRoleRequest request
    ) {
        this.userFacade.assignUserRole(
                username,
                request.applicationCode(),
                request.roleCode()
        );
    }
}
