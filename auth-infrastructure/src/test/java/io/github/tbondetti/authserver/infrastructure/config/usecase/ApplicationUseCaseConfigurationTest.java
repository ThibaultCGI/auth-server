package io.github.tbondetti.authserver.infrastructure.config.usecase;

import io.github.tbondetti.authserver.core.port.ApplicationRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.CreateApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ApplicationUseCaseConfigurationTest {

    @InjectMocks
    private ApplicationUseCaseConfiguration subject;

    @Mock
    private ApplicationRepositoryPort applicationRepositoryPort;

    @Test
    void createApplicationUseCaseShouldReturnCreateApplicationUseCase() {
        final CreateApplicationUseCase actual = this.subject.createApplicationUseCase(this.applicationRepositoryPort);

        assertNotNull(actual);
        assertInstanceOf(CreateApplicationUseCase.class, actual);
    }

    @Test
    void getApplicationUseCaseShouldReturnGetApplicationUseCase() {
        final GetApplicationUseCase actual = this.subject.getApplicationUseCase(this.applicationRepositoryPort);

        assertNotNull(actual);
        assertInstanceOf(GetApplicationUseCase.class, actual);
    }
}