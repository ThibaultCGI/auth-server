package io.github.tbondetti.authserver.infrastructure.config;

import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.adapter.UserRepositoryAdapter;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

    @Bean
    UserRepositoryPort userRepositoryPort(final UserJpaRepository userJpaRepository) {
        return new UserRepositoryAdapter(userJpaRepository);
    }

}
