package io.github.tbondetti.authserver.infrastructure.web.error;


import io.github.tbondetti.authserver.core.exception.AuthServerException;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(AuthServerFunctionalException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthServerException(final AuthServerException e) {
        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(e.getCode())
                .description(e.getMessage())
                .build();

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthServerException(final UsernameNotFoundException e) {
        final AuthServerException cause = (AuthServerException) e.getCause();
        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(cause.getCode())
                .description(e.getMessage())
                .build();

        return ResponseEntity
                .badRequest()
                .body(body);
    }

}
