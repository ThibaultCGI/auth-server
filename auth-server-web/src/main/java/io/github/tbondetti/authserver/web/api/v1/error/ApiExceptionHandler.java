package io.github.tbondetti.authserver.web.api.v1.error;

import io.github.tbondetti.authserver.core.exception.AuthServerException;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.exception.AuthServerNotFoundException;
import io.github.tbondetti.authserver.core.exception.AuthServerTechnicalException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.FORMAT_DONNEE_INCORRECT;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
            final ConstraintViolationException exception
    ) {
        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(VALIDATION_ERROR)
                .description(exception.getConstraintViolations().stream().toList().getFirst().getMessage())
                .build()
                ;
        return ResponseEntity
                .badRequest()
                .body(body)
                ;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception
    ) {

        final String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(FORMAT_DONNEE_INCORRECT)
                ;

        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(VALIDATION_ERROR)
                .description(message)
                .build()
                ;
        return ResponseEntity
                .badRequest()
                .body(body)
                ;
    }

    @ExceptionHandler(AuthServerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(final AuthServerNotFoundException e) {
        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(e.getCode())
                .description(e.getMessage())
                .build();

        return ResponseEntity
                .status(NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(AuthServerFunctionalException.class)
    public ResponseEntity<ApiErrorResponse> handleFunctionalException(final AuthServerException e) {
        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(e.getCode())
                .description(e.getMessage())
                .build();

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    @ExceptionHandler(AuthServerTechnicalException.class)
    public ResponseEntity<ApiErrorResponse> handleTechnicalException(final AuthServerTechnicalException e) {
        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(e.getCode())
                .description(e.getMessage())
                .build();

        return ResponseEntity
                .internalServerError()
                .body(body);
    }
}
