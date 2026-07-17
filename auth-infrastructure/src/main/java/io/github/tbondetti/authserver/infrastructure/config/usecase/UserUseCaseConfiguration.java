package io.github.tbondetti.authserver.infrastructure.config.usecase;

import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.user.AssignRoleToUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.CreateUserUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserRolesForApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfiguration {

    @Bean
    CreateUserUseCase createUserUseCase(
            final UserRepositoryPort userRepositoryPort,
            final PasswordEncoderPort passwordEncoderPort
    ) {
        return new CreateUserUseCase(userRepositoryPort, passwordEncoderPort);
    }

    @Bean
    GetUserUseCase getUserUseCase(final UserRepositoryPort userRepositoryPort) {
        return new GetUserUseCase(userRepositoryPort);
    }

    @Bean
    AssignRoleToUserUseCase assignRoleToUserUseCase(
            final UserRepositoryPort userRepositoryPort,
            final GetUserUseCase getUserUseCase,
            final GetApplicationUseCase getApplicationUseCase,
            final GetRoleUseCase getRoleUseCase,
            final RoleRepositoryPort roleRepositoryPort
    ) {
        return new AssignRoleToUserUseCase(
                userRepositoryPort,
                getUserUseCase,
                getApplicationUseCase,
                getRoleUseCase,
                roleRepositoryPort
        );
    }

    @Bean
    GetUserRolesForApplicationUseCase getUserRolesForApplicationUseCase(
            final UserRepositoryPort userRepositoryPort,
            final GetUserUseCase getUserUseCase,
            final GetApplicationUseCase getApplicationUseCase
    ) {
        return new GetUserRolesForApplicationUseCase(
                userRepositoryPort,
                getUserUseCase,
                getApplicationUseCase
        );
    }
}
