package io.github.tbondetti.authserver.infrastructure.config.usecase;

import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.role.CreateRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.role.DeleteRoleUseCase;
import io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleUseCaseConfiguration {

    @Bean
    CreateRoleUseCase createRoleUseCase(
            final RoleRepositoryPort roleRepositoryPort,
            final GetApplicationUseCase getApplicationUseCase
    ) {
        return new CreateRoleUseCase(roleRepositoryPort, getApplicationUseCase);
    }

    @Bean
    GetRoleUseCase getRoleUseCase(
            final RoleRepositoryPort roleRepositoryPort,
            final GetApplicationUseCase getApplicationUseCase
    ) {
        return new GetRoleUseCase(roleRepositoryPort, getApplicationUseCase);
    }

    @Bean
    DeleteRoleUseCase deleteRoleUseCase(
            final RoleRepositoryPort roleRepositoryPort,
            final GetRoleUseCase getRoleUseCase
    ) {
        return new DeleteRoleUseCase(roleRepositoryPort, getRoleUseCase);
    }

}
