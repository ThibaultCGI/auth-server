package io.github.tbondetti.authserver.web.api.v1.error;

import io.github.tbondetti.authserver.core.exception.AuthServerException;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.exception.AuthServerNotFoundException;
import io.github.tbondetti.authserver.core.exception.AuthServerTechnicalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ApiExceptionHandler {

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
