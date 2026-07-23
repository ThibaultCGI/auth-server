package io.github.tbondetti.authserver.infrastructure.security.encoder;

import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(final String rawPassword)
    {
        // pour une même entrée, this.passwordEncoder.encode(.) génère un nouveau hash à chaque fois
        return this.passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(
            final String rawPassword,
            final String encodedPassword
    ) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
