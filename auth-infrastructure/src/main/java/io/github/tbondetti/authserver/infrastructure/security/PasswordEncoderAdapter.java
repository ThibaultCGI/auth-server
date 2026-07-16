package io.github.tbondetti.authserver.infrastructure.security;

import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(final String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

}
