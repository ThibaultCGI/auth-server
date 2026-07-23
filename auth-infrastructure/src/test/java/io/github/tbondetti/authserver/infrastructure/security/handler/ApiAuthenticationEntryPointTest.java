package io.github.tbondetti.authserver.infrastructure.security.handler;

import io.github.tbondetti.authserver.infrastructure.web.error.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.INVALID_CREDENTIALS;
import static io.github.tbondetti.authserver.infrastructure.security.SecurityMessages.ERROR_INVALID_CREDENTIALS;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class ApiAuthenticationEntryPointTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ApiAuthenticationEntryPoint subject = new ApiAuthenticationEntryPoint(this.objectMapper);

    @Test
    void commenceShouldReturnUnauthorizedWithJsonBody() throws IOException {
        final HttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AuthenticationException authenticationException = new BadCredentialsException("Bad credentials");

        this.subject.commence(request, response, authenticationException);

        assertEquals(SC_UNAUTHORIZED, response.getStatus());
        assertEquals(APPLICATION_JSON_VALUE + ";charset=" + UTF_8, response.getContentType());

        final ApiErrorResponse actualBody = this.objectMapper.readValue(
                response.getContentAsByteArray(),
                ApiErrorResponse.class
        );

        assertEquals(INVALID_CREDENTIALS, actualBody.code());
        assertEquals(ERROR_INVALID_CREDENTIALS, actualBody.description());
    }

    @Test
    void commenceShouldPropagateIOExceptionWhenWritingResponseFails() throws IOException {
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final AuthenticationException authenticationException = new BadCredentialsException("Bad credentials");

        when(response.getOutputStream()).thenThrow(new IOException("Unable to write response"));

        assertThatThrownBy(() -> this.subject.commence(request, response, authenticationException))
                .isInstanceOf(IOException.class)
                .hasMessage("Unable to write response");
    }
}