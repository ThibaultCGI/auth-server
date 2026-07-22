package io.github.tbondetti.authserver.core.exception;

import org.junit.jupiter.api.Test;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CODE_IS_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertSame;

class AuthServerFunctionalExceptionTest {

    @Test
    void constructorOk() {
        final String message = "message";
        final AuthServerErrorCode code = CODE_IS_REQUIRED;
        final AuthServerFunctionalException exception1 = new AuthServerFunctionalException(code, message);

        assertSame(code, exception1.getCode());
        assertSame(message, exception1.getMessage());

        final AuthServerFunctionalException exception2 = new AuthServerFunctionalException(code, message, exception1);
        assertSame(code, exception2.getCode());
        assertSame(message, exception2.getMessage());
        assertSame(exception1, exception2.getCause());
    }

}