package io.github.tbondetti.authserver.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

import static java.time.Clock.systemUTC;

@Configuration
public class SpringConfiguration {

    @Bean
    Clock clock() {
        return systemUTC();
    }

}
