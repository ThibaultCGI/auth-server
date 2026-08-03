package io.github.tbondetti.authserver.web.api.controller;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.web.api.dto.CreateApplicationRequest;
import io.github.tbondetti.authserver.web.api.mapper.ApplicationWebMapper;
import io.github.tbondetti.authserver.web.api.response.ApplicationResponse;
import io.github.tbondetti.authserver.web.facade.ApplicationFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.tbondetti.authserver.web.api.mapper.ApplicationWebMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {

    @InjectMocks
    private ApplicationController subject;

    @Mock
    private ApplicationFacade applicationFacade;

    @Test
    void getApplicationOk() {
        final String code = "code";

        final Application application = Application.builder().build();
        when(this.applicationFacade.getApplication(code)).thenReturn(application);

        final ApplicationResponse applicationResponse = ApplicationResponse.builder().build();
        try (final MockedStatic<ApplicationWebMapper> userUtilities = mockStatic(ApplicationWebMapper.class)) {
            userUtilities.when(() -> toResponse(application)).thenReturn(applicationResponse); // déjà testé

            assertSame(applicationResponse, this.subject.getApplication(code));
        }
    }

    @Test
    void createUserOk() {
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest(code, name, description);

        final Application application = Application.builder().build();
        when(this.applicationFacade.createApplication(code, name, description)).thenReturn(application);

        final ApplicationResponse applicationResponse = ApplicationResponse.builder().build();
        try (final MockedStatic<ApplicationWebMapper> userUtilities = mockStatic(ApplicationWebMapper.class)) {
            userUtilities.when(() -> toResponse(application)).thenReturn(applicationResponse); // déjà testé

            assertSame(applicationResponse, this.subject.createApplication(createApplicationRequest));
        }
    }

}