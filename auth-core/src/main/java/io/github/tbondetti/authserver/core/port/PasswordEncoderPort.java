package io.github.tbondetti.authserver.core.port;

public interface PasswordEncoderPort {

    String encode(String rawPassword);
}
