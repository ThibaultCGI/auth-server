package io.github.tbondetti.authserver.web.api.v1.error;

import io.github.tbondetti.authserver.core.exception.AuthServerErrorCode;
import io.github.tbondetti.authserver.core.exception.AuthServerException;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.exception.AuthServerNotFoundException;
import io.github.tbondetti.authserver.core.exception.AuthServerTechnicalException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Set;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.FORMAT_DONNEE_INCORRECT;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NOT_FOUND;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.USER_NOT_FOUND;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.VALIDATION_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler subject;

    @Test
    void handleConstraintViolationExceptionOk() {
        final String message = "Application introuvable.";

        final ConstraintViolation<?> violation = mock(ConstraintViolation.class);

        when(violation.getMessage()).thenReturn(message);

        final ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        final ResponseEntity<ApiErrorResponse> actual = this.subject.handleConstraintViolationException(exception);

        assertEquals(BAD_REQUEST.value(), actual.getStatusCode().value());

        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(VALIDATION_ERROR)
                .description(message)
                .build()
                ;

        assertEquals(body, actual.getBody());
    }

    @Test
    void handleMethodArgumentNotValidExceptionWithNoFieldErrorOk() {
        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");

        final MethodParameter parameter = mock(MethodParameter.class);
        final MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        final ResponseEntity<ApiErrorResponse> actual = this.subject.handleMethodArgumentNotValidException(exception);

        assertEquals(BAD_REQUEST.value(), actual.getStatusCode().value());

        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(VALIDATION_ERROR)
                .description(FORMAT_DONNEE_INCORRECT)
                .build();

        assertEquals(body, actual.getBody());
    }

    @Test
    void handleMethodArgumentNotValidExceptionOk() {
        final String message = "Le nom du produit est obligatoire.";

        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");

        bindingResult.addError(new FieldError("request", "nom", message));

        final MethodParameter parameter = mock(MethodParameter.class);
        final MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        final ResponseEntity<ApiErrorResponse> actual = this.subject.handleMethodArgumentNotValidException(exception);

        assertEquals(BAD_REQUEST.value(), actual.getStatusCode().value());

        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(VALIDATION_ERROR)
                .description(message)
                .build();

        assertEquals(body, actual.getBody());
    }

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