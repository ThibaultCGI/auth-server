package io.github.tbondetti.authserver.core.exception;

public class AuthServerTechnicalException extends AuthServerException {

    public AuthServerTechnicalException(
            final AuthServerErrorCode code,
            final String message
    ) {
        super(code, message);
    }

    public AuthServerTechnicalException(
            final AuthServerErrorCode code,
            final String message,
            final Throwable cause
    ) {
        super(code, message, cause);
    }
}
