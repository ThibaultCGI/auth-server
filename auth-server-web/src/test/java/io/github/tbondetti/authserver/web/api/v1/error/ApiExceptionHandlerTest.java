package io.github.tbondetti.authserver.web.api.v1.error;

import io.github.tbondetti.authserver.core.exception.AuthServerErrorCode;
import io.github.tbondetti.authserver.core.exception.AuthServerException;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.exception.AuthServerNotFoundException;
import io.github.tbondetti.authserver.core.exception.AuthServerTechnicalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NOT_FOUND;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler subject;

    @Test
    void handleNotFoundExceptionOk() {
        final AuthServerErrorCode code = CLIENT_NOT_FOUND;
        final String message = "Client introuvable";

        final AuthServerNotFoundException exception = new AuthServerNotFoundException(code, message);

        final ResponseEntity<ApiErrorResponse> actual = this.subject.handleNotFoundException(exception);

        assertEquals(NOT_FOUND.value(), actual.getStatusCode().value());

        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(code)
                .description(message)
                .build()
                ;

        assertEquals(body, actual.getBody());
    }


    @Test
    void handleFunctionalExceptionOk() {
        final AuthServerErrorCode code = USER_NOT_FOUND;
        final String message = "Utilisateur introuvable";

        final AuthServerException exception = new AuthServerFunctionalException(code, message);

        final ResponseEntity<ApiErrorResponse> actual = this.subject.handleFunctionalException(exception);

        assertEquals(BAD_REQUEST.value(), actual.getStatusCode().value());

        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(code)
                .description(message)
                .build();

        assertEquals(body, actual.getBody());
    }

    @Test
    void handleTechnicalExceptionOk() {
        final AuthServerErrorCode code = CLIENT_NOT_FOUND;
        final String message = "Client introuvable";

        final AuthServerTechnicalException exception = new AuthServerTechnicalException(code, message);

        final ResponseEntity<ApiErrorResponse> actual = this.subject.handleTechnicalException(exception);

        assertEquals(INTERNAL_SERVER_ERROR.value(), actual.getStatusCode().value());

        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(code)
                .description(message)
                .build();

        assertEquals(body, actual.getBody());
    }
}