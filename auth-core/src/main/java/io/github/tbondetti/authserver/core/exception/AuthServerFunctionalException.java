package io.github.tbondetti.authserver.core.exception;

public class AuthServerFunctionalException extends AuthServerException {

    public AuthServerFunctionalException(final String message) {
        super(message);
    }

    public AuthServerFunctionalException(final String message,
                                         final Throwable cause
    ) {
        super(message, cause);
    }
}
