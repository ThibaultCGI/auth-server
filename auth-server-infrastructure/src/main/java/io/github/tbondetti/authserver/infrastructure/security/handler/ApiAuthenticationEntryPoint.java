package io.github.tbondetti.authserver.infrastructure.security.handler;

import io.github.tbondetti.authserver.infrastructure.web.error.ApiErrorResponse;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.INVALID_CREDENTIALS;
import static io.github.tbondetti.authserver.infrastructure.security.SecurityMessages.ERROR_INVALID_CREDENTIALS;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            @Nonnull final HttpServletRequest request,
            @Nonnull final HttpServletResponse response,
            @Nonnull final AuthenticationException authException
    ) throws IOException {

        final ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(INVALID_CREDENTIALS)
                .description(ERROR_INVALID_CREDENTIALS)
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8);

        this.objectMapper.writeValue(
                response.getOutputStream(),
                apiErrorResponse
        );
    }
}