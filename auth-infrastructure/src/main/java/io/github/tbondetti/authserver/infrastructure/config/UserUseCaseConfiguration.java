package io.github.tbondetti.authserver.infrastructure.config;

import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.CreateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UserUseCaseConfiguration {

    @Bean
    CreateUserUseCase createUserUseCase(final UserRepositoryPort userRepositoryPort,
                                        final PasswordEncoderPort passwordEncoderPort,
                                        final Clock clock
    ) {
        return new CreateUserUseCase(userRepositoryPort, passwordEncoderPort, clock);
    }
}
