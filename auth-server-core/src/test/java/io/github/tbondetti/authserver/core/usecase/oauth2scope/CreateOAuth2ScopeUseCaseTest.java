package io.github.tbondetti.authserver.core.usecase.oauth2scope;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.OAuth2ScopeRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_CODE_ALREADY_EXISTS_FOR_APPLICATION;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_CODE_ALREADY_EXISTS_FOR_APPLICATION;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.validateAndNormalizeName;
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
class CreateOAuth2ScopeUseCaseTest {

    @Spy
    @InjectMocks
    private CreateOAuth2ScopeUseCase subject;

    @Mock
    private OAuth2ScopeRepositoryPort oauth2ScopeRepositoryPort;

    @Mock
    private GetApplicationUseCase getApplicationUseCase;


    @Test
    void ensureCodeIsUniqueForApplicationKo() {
        final String applicationCode = "applicationCode";
        final String scopeCode = "scopeCode";

        final OAuth2Scope existingScope = OAuth2Scope.builder().build();

        when(this.oauth2ScopeRepositoryPort.findByApplicationCodeAndCode(
                applicationCode,
                scopeCode
        )).thenReturn(Optional.of(existingScope));

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> this.subject.ensureCodeIsUniqueForApplication(applicationCode, scopeCode)
        );

        assertSame(SCOPE_CODE_ALREADY_EXISTS_FOR_APPLICATION, exception.getCode());
        assertEquals(ERROR_SCOPE_CODE_ALREADY_EXISTS_FOR_APPLICATION, exception.getMessage());
    }

    @Test
    void ensureCodeIsUniqueForApplicationOk() {
        final String applicationCode = "applicationCode";
        final String scopeCode = "scopeCode";

        when(this.oauth2ScopeRepositoryPort.findByApplicationCodeAndCode(
                applicationCode,
                scopeCode
        )).thenReturn(Optional.empty());

        this.subject.ensureCodeIsUniqueForApplication(applicationCode, scopeCode);

        verify(this.oauth2ScopeRepositoryPort, times(1)).findByApplicationCodeAndCode(
                applicationCode,
                scopeCode
        );
    }

    @Test
    void executeOk() {
        final String givenApplicationCode = "givenApplicationCode";
        final String givenCode = "givenCode";
        final String givenName = "givenName";
        final String givenDescription = "givenDescription";

        final String normalizedApplicationCode = "normalizedApplicationCode";

        final Application application = Application.builder()
                .code(normalizedApplicationCode)
                .build();

        when(this.getApplicationUseCase.execute(givenApplicationCode)).thenReturn(application);

        final String normalizedCode = "normalizedCode";
        final String normalizedName = "normalizedName";
        final String normalizedDescription = "normalizedDescription";

        doNothing().when(this.subject).ensureCodeIsUniqueForApplication(
                normalizedApplicationCode,
                normalizedCode
        );

        final UUID uuid = randomUUID();

        final OAuth2Scope scopeToSave = OAuth2Scope.builder()
                .id(uuid)
                .applicationCode(normalizedApplicationCode)
                .code(normalizedCode)
                .name(normalizedName)
                .description(normalizedDescription)
                .build();

        final OAuth2Scope savedScope = OAuth2Scope.builder().build();

        when(this.oauth2ScopeRepositoryPort.save(scopeToSave)).thenReturn(savedScope);

        try (final MockedStatic<OAuth2ScopeValidationUtils> validationUtils = mockStatic(OAuth2ScopeValidationUtils.class);
             final MockedStatic<UUID> uuidUtilities = mockStatic(UUID.class)
        ) {

            validationUtils.when(() -> validateAndNormalizeCode(givenCode)).thenReturn(normalizedCode);
            validationUtils.when(() -> validateAndNormalizeName(givenName)).thenReturn(normalizedName);
            validationUtils.when(() -> normalizeAndValidateDescription(givenDescription)).thenReturn(normalizedDescription);

            uuidUtilities.when(UUID::randomUUID).thenReturn(uuid);

            assertSame(savedScope, this.subject.execute(
                    givenApplicationCode,
                    givenCode,
                    givenName,
                    givenDescription
            ));
        }

        verify(this.subject, times(1)).ensureCodeIsUniqueForApplication(
                normalizedApplicationCode,
                normalizedCode
        );
    }
}