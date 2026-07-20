package io.github.tbondetti.authserver.infrastructure.web.controller;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.usecase.user.AssignRoleToUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.CreateUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
import io.github.tbondetti.authserver.infrastructure.web.dto.AssignRoleRequest;
import io.github.tbondetti.authserver.infrastructure.web.dto.CreateUserRequest;
import io.github.tbondetti.authserver.infrastructure.web.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.UserWebMapper.toResponse;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final GetUserUseCase getUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final AssignRoleToUserUseCase assignRoleToUserUseCase;

    @GetMapping("/{username}")
    public UserResponse getUser(@PathVariable final String username) {
        return toResponse(this.getUserUseCase.execute(username));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody final CreateUserRequest request) {
        final User user = this.createUserUseCase.execute(
                request.username(),
                request.password()
        );

        return toResponse(user);
    }


    @PostMapping("/{username}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignRole(
            @PathVariable final String username,
            @RequestBody final AssignRoleRequest request
    ) {
        this.assignRoleToUserUseCase.execute(
                username,
                request.roleCode(),
                request.applicationCode()
        );
    }
}
