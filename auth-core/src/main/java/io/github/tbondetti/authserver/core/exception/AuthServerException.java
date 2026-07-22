package io.github.tbondetti.authserver.core.exception;

import lombok.Getter;

public class AuthServerException extends RuntimeException {

    @Getter
    private final AuthServerErrorCode code;

    public AuthServerException(final AuthServerErrorCode code,
                               final String message
    ) {
        this.code = code;
        super(message);
    }

    public AuthServerException(final AuthServerErrorCode code,
                               final String message,
                               final Throwable cause
    ) {
        this.code = code;
        super(message, cause);
    }
}
