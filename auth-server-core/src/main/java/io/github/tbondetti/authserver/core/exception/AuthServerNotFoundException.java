package io.github.tbondetti.authserver.core.exception;

@SuppressWarnings("java:S110")
public class AuthServerNotFoundException extends AuthServerFunctionalException {

    public AuthServerNotFoundException(
            final AuthServerErrorCode code,
            final String message
    ) {
        super(code, message);
    }

    public AuthServerNotFoundException(
            final AuthServerErrorCode code,
            final String message,
            final Throwable cause
    ) {
        super(code, message, cause);
    }
}
