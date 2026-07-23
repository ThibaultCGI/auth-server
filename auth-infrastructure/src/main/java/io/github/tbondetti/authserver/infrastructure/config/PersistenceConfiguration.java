package io.github.tbondetti.authserver.infrastructure.config;

import io.github.tbondetti.authserver.core.port.ApplicationRepositoryPort;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.adapter.ApplicationRepositoryAdapter;
import io.github.tbondetti.authserver.infrastructure.persistence.adapter.OAuth2ClientRepositoryAdapter;
import io.github.tbondetti.authserver.infrastructure.persistence.adapter.RoleRepositoryAdapter;
import io.github.tbondetti.authserver.infrastructure.persistence.adapter.UserRepositoryAdapter;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.OAuth2ClientJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.RoleJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserRoleJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

    @Bean
    UserRepositoryPort userRepositoryPort(
            final UserJpaRepository userJpaRepository,
            final RoleJpaRepository roleJpaRepository,
            final UserRoleJpaRepository userRoleJpaRepository
    ) {
        return new UserRepositoryAdapter(
                userJpaRepository,
                roleJpaRepository,
                userRoleJpaRepository
        );
    }

    @Bean
    ApplicationRepositoryPort applicationRepositoryPort(final ApplicationJpaRepository applicationJpaRepository) {
        return new ApplicationRepositoryAdapter(applicationJpaRepository);
    }

    @Bean
    RoleRepositoryPort roleRepositoryPort(
            final ApplicationJpaRepository applicationJpaRepository,
            final RoleJpaRepository roleJpaRepository,
            final UserRoleJpaRepository userRoleJpaRepository
    ) {
        return new RoleRepositoryAdapter(
                applicationJpaRepository,
                roleJpaRepository,
                userRoleJpaRepository
        );
    }

    @Bean
    OAuth2ClientRepositoryPort oauth2ClientRepositoryPort(
            final OAuth2ClientJpaRepository oauth2ClientJpaRepository,
            final ApplicationJpaRepository applicationJpaRepository
    ) {
        return new OAuth2ClientRepositoryAdapter(
                oauth2ClientJpaRepository,
                applicationJpaRepository
        );
    }


}
