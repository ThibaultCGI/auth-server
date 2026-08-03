package io.github.tbondetti.authserver.web.security.handler;

import io.github.tbondetti.authserver.web.api.error.ApiErrorResponse;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.ACCESS_DENIED;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
@RequiredArgsConstructor
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

    static final String ACCESS_DENIED_MESSAGE = "Accès refusé";

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            @Nonnull final HttpServletRequest request,
            @Nonnull final HttpServletResponse response,
            @Nonnull final AccessDeniedException accessDeniedException
    ) throws IOException {

        final ApiErrorResponse body = ApiErrorResponse.builder()
                .code(ACCESS_DENIED)
                .description(ACCESS_DENIED_MESSAGE)
                .build();

        response.setStatus(SC_FORBIDDEN);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        this.objectMapper.writeValue(
                response.getOutputStream(),
                body
        );
    }
}