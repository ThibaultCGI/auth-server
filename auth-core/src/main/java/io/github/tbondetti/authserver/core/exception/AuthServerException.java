package io.github.tbondetti.authserver.core.exception;

public class AuthServerException extends RuntimeException {

    public AuthServerException(final String message) {
        super(message);
    }

    public AuthServerException(final String message,
                               final Throwable cause
    ) {
        super(message, cause);
    }
}
