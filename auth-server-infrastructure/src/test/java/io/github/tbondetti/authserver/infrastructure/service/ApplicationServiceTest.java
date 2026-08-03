package io.github.tbondetti.authserver.infrastructure.service;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.usecase.application.CreateApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @InjectMocks
    private ApplicationService subject;

    @Mock
    private CreateApplicationUseCase createApplicationUseCase;

    @Mock
    private GetApplicationUseCase getApplicationUseCase;

    @Test
    void getApplicationOK() {
        final String givenCode = "givenCode";
        final Application application = Application.builder().build();
        when(this.getApplicationUseCase.execute(givenCode)).thenReturn(application);
        assertSame(application, this.subject.getApplication(givenCode));
    }

    @Test
    void createApplicationOk() {
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final Application application = Application.builder().build();
        when(this.createApplicationUseCase.execute(code, name, description)).thenReturn(application);

        assertSame(application, this.subject.createApplication(code, name, description));



    }
}