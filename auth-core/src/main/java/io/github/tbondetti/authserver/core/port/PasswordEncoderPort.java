package io.github.tbondetti.authserver.core.port;

public interface PasswordEncoderPort {

    String encode(final String rawPassword);

    boolean matches(
            final String rawPassword,
            final String encodedPassword
    );
}
