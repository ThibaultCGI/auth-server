package io.github.tbondetti.authserver.core.exception;

import org.junit.jupiter.api.Test;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertSame;

class AuthServerNotFoundExceptionTest {

    @Test
    void constructorOk() {
        final String message = "message";
        final AuthServerErrorCode code = CLIENT_NOT_FOUND;
        final AuthServerNotFoundException exception1 = new AuthServerNotFoundException(code, message);

        assertSame(code, exception1.getCode());
        assertSame(message, exception1.getMessage());

        final AuthServerNotFoundException exception2 = new AuthServerNotFoundException(code, message, exception1);
        assertSame(code, exception2.getCode());
        assertSame(message, exception2.getMessage());
        assertSame(exception1, exception2.getCause());
    }
}