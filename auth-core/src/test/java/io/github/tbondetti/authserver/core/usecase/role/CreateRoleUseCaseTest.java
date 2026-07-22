package io.github.tbondetti.authserver.core.usecase.role;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
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

import static io.github.tbondetti.authserver.core.constants.RoleRules.CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.RoleRules.DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.RoleRules.NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.ROLE_CODE_ALREADY_EXISTS;
import static io.github.tbondetti.authserver.core.usecase.role.CreateRoleUseCase.ERROR_CODE_MUST_BE_UNIQUE;
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
class CreateRoleUseCaseTest {

    @Spy
    @InjectMocks
    private CreateRoleUseCase subject;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;

    @Mock
    private GetApplicationUseCase getApplicationUseCase;


    @Test
    void ensureCodeIsUniqueForApplicationKo() {
        final String givenApplicationCode = "givenApplicationCode";
        final String givenNormalizedCode = "givenNormalizedCode";

        final Role role = Role.builder().build();
        when(this.roleRepositoryPort.findByApplicationCodeAndCode(givenApplicationCode, givenNormalizedCode)).thenReturn(Optional.of(role));

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> this.subject.ensureCodeIsUniqueForApplication(givenApplicationCode, givenNormalizedCode)
        );

        assertSame(ROLE_CODE_ALREADY_EXISTS, exception.getCode());
        assertEquals(ERROR_CODE_MUST_BE_UNIQUE.formatted(givenApplicationCode), exception.getMessage());
    }

    @Test
    void ensureCodeIsUniqueForApplicationOk() {
        final String givenApplicationCode = "givenApplicationCode";
        final String givenNormalizedCode = "givenNormalizedCode";

        when(this.roleRepositoryPort.findByApplicationCodeAndCode(givenApplicationCode, givenNormalizedCode)).thenReturn(Optional.empty());

        this.subject.ensureCodeIsUniqueForApplication(givenApplicationCode, givenNormalizedCode);

        verify(this.roleRepositoryPort, times(1)).findByApplicationCodeAndCode(givenApplicationCode, givenNormalizedCode);
    }

    @Test
    void executeOk() {
        final String givenApplicationCode = "givenApplicationCode";
        final String givenCode = "givenCode";
        final String givenName = "givenName";
        final String givenDescription = "givenDescription";

        final String codeApplication = "codeApplication";
        final Application application = Application.builder().code(codeApplication).build();
        when(this.getApplicationUseCase.execute(givenApplicationCode)).thenReturn(application);

        final String normalizedCode = "normalizedCode";
        final String normalizedName = "normalizedName";
        final String normalizedDescription = "normalizedDescription";


        doNothing().when(this.subject).ensureCodeIsUniqueForApplication(codeApplication, normalizedCode);

        final UUID uuid = randomUUID();

        final Role roleToSave = Role.builder()
                .codeApplication(codeApplication)
                .id(uuid)
                .code(normalizedCode)
                .name(normalizedName)
                .description(normalizedDescription)
                .build();

        final Role roleSaved = Role.builder().build();
        when(this.roleRepositoryPort.save(roleToSave)).thenReturn(roleSaved);

        try (final MockedStatic<CommonValidationUtils> commonUtilities = mockStatic(CommonValidationUtils.class);
             final MockedStatic<UUID> uUIDUtilities = mockStatic(UUID.class)
        ) {
            commonUtilities.when(() -> validateAndNormalizeCode(givenCode, CODE_MAX_LENGTH)).thenReturn(normalizedCode);
            commonUtilities.when(() -> validateAndNormalizeName(givenName, NAME_MAX_LENGTH)).thenReturn(normalizedName);
            commonUtilities.when(() -> normalizeAndValidateDescription(givenDescription, DESCRIPTION_MAX_LENGTH)).thenReturn(normalizedDescription);
            uUIDUtilities.when(UUID::randomUUID).thenReturn(uuid);

            assertSame(roleSaved, this.subject.execute(givenApplicationCode, givenCode, givenName, givenDescription));

        }

        verify(this.subject, times(1)).ensureCodeIsUniqueForApplication(codeApplication, normalizedCode);
    }
}