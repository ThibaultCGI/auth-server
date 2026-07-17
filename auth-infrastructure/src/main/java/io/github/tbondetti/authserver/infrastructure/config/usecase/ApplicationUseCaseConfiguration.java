package io.github.tbondetti.authserver.infrastructure.config.usecase;

import io.github.tbondetti.authserver.core.port.ApplicationRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.CreateApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationUseCaseConfiguration {

    @Bean
    CreateApplicationUseCase createApplicationUseCase(final ApplicationRepositoryPort applicationRepositoryPort) {
        return new CreateApplicationUseCase(applicationRepositoryPort);
    }

    @Bean
    GetApplicationUseCase getApplicationUseCase(final ApplicationRepositoryPort applicationRepositoryPort
    ) {
        return new GetApplicationUseCase(applicationRepositoryPort);
    }

}
