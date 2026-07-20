package io.github.tbondetti.authserver.core.usecase.application;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.ApplicationRepositoryPort;
import io.github.tbondetti.authserver.core.utils.CommonValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase.ERROR_APPLICATION_NOT_FOUND;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetApplicationUseCaseTest {

    @InjectMocks
    private GetApplicationUseCase subject;

    @Mock
    private ApplicationRepositoryPort applicationRepositoryPort;

    @Test
    void executeKo() {
        final String givenCode = "givenCode";


        try(final MockedStatic<CommonValidationUtils> utilities = mockStatic(CommonValidationUtils.class)) {
            final String normalizedCode = "normalizedCode";
            utilities.when(() -> normalizeCode(givenCode)).thenReturn(normalizedCode);

            when(this.applicationRepositoryPort.findByCode(normalizedCode)).thenReturn(Optional.empty());

            final AuthServerFunctionalException exception = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> this.subject.execute(givenCode)
            );

            assertEquals(ERROR_APPLICATION_NOT_FOUND.formatted(normalizedCode), exception.getMessage());
        }

    }

    @Test
    void executeOk() {
        final String givenCode = "givenCode";

        try(final MockedStatic<CommonValidationUtils> utilities = mockStatic(CommonValidationUtils.class)) {
            final String normalizedCode = "normalizedCode";
            utilities.when(() -> normalizeCode(givenCode)).thenReturn(normalizedCode);

            final Application application = Application.builder().build();
            when(this.applicationRepositoryPort.findByCode(normalizedCode)).thenReturn(Optional.of(application));

            assertSame(application, this.subject.execute(givenCode));
        }
    }
}