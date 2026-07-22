package io.github.tbondetti.authserver.infrastructure.web.error;

import io.github.tbondetti.authserver.core.exception.AuthServerErrorCode;
import io.github.tbondetti.authserver.core.exception.AuthServerException;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler subject;

    @Test
    void handleAuthServerExceptionOk() {
        final AuthServerErrorCode code = USER_NOT_FOUND;
        final String message = "Utilisateur introuvable";

        final AuthServerException exception = new AuthServerFunctionalException(code, message);

        final ResponseEntity<ApiErrorResponse> actual = this.subject.handleAuthServerException(exception);

        assertEquals(BAD_REQUEST.value(), actual.getStatusCode().value());

        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(code)
                .description(message)
                .build();

        assertEquals(body, actual.getBody());
    }
}