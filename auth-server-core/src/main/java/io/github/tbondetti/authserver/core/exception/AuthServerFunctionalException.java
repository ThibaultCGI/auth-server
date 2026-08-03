package io.github.tbondetti.authserver.core.exception;

public class AuthServerFunctionalException extends AuthServerException {

    public AuthServerFunctionalException(
            final AuthServerErrorCode code,
            final String message
    ) {
        super(code, message);
    }

    public AuthServerFunctionalException(
            final AuthServerErrorCode code,
            final String message,
            final Throwable cause
    ) {
        super(code, message, cause);
    }
}
