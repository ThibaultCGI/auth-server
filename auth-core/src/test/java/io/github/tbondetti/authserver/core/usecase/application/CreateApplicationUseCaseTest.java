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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.APPLICATION_CODE_ALREADY_EXISTS;
import static io.github.tbondetti.authserver.core.usecase.application.CreateApplicationUseCase.ERROR_CODE_MUST_BE_UNIQUE;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.validateAndNormalizeName;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateApplicationUseCaseTest {

    @Spy
    @InjectMocks
    private CreateApplicationUseCase subject;

    @Mock
    private ApplicationRepositoryPort applicationRepositoryPort;

    @Test
    void ensureCodeIsUniqueKo() {
        final String normalizedCode = "normalizedCode";

        final Application application = Application.builder().build();
        when(this.applicationRepositoryPort.findByCode(normalizedCode)).thenReturn(Optional.of(application));

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> this.subject.ensureCodeIsUnique(normalizedCode)
        );

        assertSame(APPLICATION_CODE_ALREADY_EXISTS, exception.getCode());
        assertEquals(ERROR_CODE_MUST_BE_UNIQUE, exception.getMessage());
    }

    @Test
    void ensureCodeIsUniqueOk() {
        final String normalizedCode = "normalizedCode";

        when(this.applicationRepositoryPort.findByCode(normalizedCode)).thenReturn(Optional.empty());

        this.subject.ensureCodeIsUnique(normalizedCode);

        verify(this.applicationRepositoryPort, times(1)).findByCode(normalizedCode);
    }

    @Test
    void executeOk() {
        final String givenCode = "givenCode";
        final String givenName = "givenName";
        final String givenDescription = "givenDescription";

        final UUID uuid = randomUUID();


        try (final MockedStatic<CommonValidationUtils> commonUtilities = mockStatic(CommonValidationUtils.class);
             final MockedStatic<UUID> uUIDUtilities = mockStatic(UUID.class)
        ) {
            final String normalizedCode = "normalizedCode";
            commonUtilities.when(() -> validateAndNormalizeCode(givenCode, CODE_MAX_LENGTH)).thenReturn(normalizedCode);

            final String normalizedName = "normalizedName";
            commonUtilities.when(() -> validateAndNormalizeName(givenName, NAME_MAX_LENGTH)).thenReturn(normalizedName);

            final String normalizedDescription = "normalizedDescription";
            commonUtilities.when(() -> normalizeAndValidateDescription(givenDescription, DESCRIPTION_MAX_LENGTH)).thenReturn(normalizedDescription);

            doNothing().when(this.subject).ensureCodeIsUnique(normalizedCode); // déjà testé

            uUIDUtilities.when(UUID::randomUUID).thenReturn(uuid);

            final Application applicationToSave = Application.builder()
                    .id(uuid)
                    .code(normalizedCode)
                    .name(normalizedName)
                    .description(normalizedDescription)
                    .build();

            final Application applicationSaved = Application.builder().build();

            when(this.applicationRepositoryPort.save(applicationToSave)).thenReturn(applicationSaved);

            assertSame(applicationSaved, this.subject.execute(givenCode, givenName, givenDescription));

            verify(this.subject, times(1)).ensureCodeIsUnique(normalizedCode);
        }
    }
}
