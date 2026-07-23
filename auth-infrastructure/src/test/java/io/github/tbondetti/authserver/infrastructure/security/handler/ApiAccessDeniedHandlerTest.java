package io.github.tbondetti.authserver.infrastructure.security.handler;

import io.github.tbondetti.authserver.infrastructure.web.error.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.ACCESS_DENIED;
import static io.github.tbondetti.authserver.infrastructure.security.handler.ApiAccessDeniedHandler.ACCES_REFUSE;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class ApiAccessDeniedHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ApiAccessDeniedHandler subject = new ApiAccessDeniedHandler(objectMapper);

    @Test
    void handleShouldReturnForbiddenWithJsonBody() throws IOException {
        final HttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AccessDeniedException exception = new AccessDeniedException("Test");

        this.subject.handle(request, response, exception);

        assertEquals(SC_FORBIDDEN, response.getStatus());
        assertEquals(APPLICATION_JSON_VALUE + ";charset=" + UTF_8, response.getContentType());

        final ApiErrorResponse actualBody = this.objectMapper.readValue(
                response.getContentAsByteArray(),
                ApiErrorResponse.class
        );

        assertEquals(ACCESS_DENIED, actualBody.code());
        assertEquals(ACCES_REFUSE, actualBody.description());
    }

    @Test
    void handleShouldPropagateIOExceptionWhenWritingResponseFails() throws IOException {
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final AccessDeniedException exception = new AccessDeniedException("Test");

        when(response.getOutputStream()).thenThrow(new IOException("Unable to write response"));

        final ApiAccessDeniedHandler failingSubject = new ApiAccessDeniedHandler(this.objectMapper);

        assertThatThrownBy(() -> failingSubject.handle(request, response, exception))
                .isInstanceOf(IOException.class)
                .hasMessage("Unable to write response");
    }
}
