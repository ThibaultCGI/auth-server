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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_NOT_FOUND;
import static io.github.tbondetti.authserver.core.usecase.oauth2scope.GetOAuth2ScopeUseCase.ERROR_SCOPE_NOT_FOUND;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.normalizeCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOAuth2ScopeUseCaseTest {

    @InjectMocks
    private GetOAuth2ScopeUseCase subject;

    @Mock
    private OAuth2ScopeRepositoryPort oauth2ScopeRepositoryPort;

    @Mock
    private GetApplicationUseCase getApplicationUseCase;


    @Test
    void executeKo() {
        final String givenApplicationCode = "givenApplicationCode";
        final String givenScopeCode = "givenScopeCode";

        final String normalizedApplicationCode = "normalizedApplicationCode";
        final String normalizedScopeCode = "normalizedScopeCode";

        final Application application = Application.builder()
                .code(normalizedApplicationCode)
                .build();

        when(this.getApplicationUseCase.execute(givenApplicationCode)).thenReturn(application);

        try (MockedStatic<OAuth2ScopeValidationUtils> utilities = mockStatic(OAuth2ScopeValidationUtils.class)) {

            utilities.when(() -> normalizeCode(givenScopeCode)).thenReturn(normalizedScopeCode);

            when(this.oauth2ScopeRepositoryPort.findByApplicationCodeAndCode(
                    normalizedApplicationCode,
                    normalizedScopeCode
            )).thenReturn(Optional.empty());

            final AuthServerFunctionalException exception = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> this.subject.execute(
                            givenApplicationCode,
                            givenScopeCode
                    )
            );

            assertSame(SCOPE_NOT_FOUND, exception.getCode());
            assertEquals(
                    ERROR_SCOPE_NOT_FOUND.formatted(normalizedScopeCode, normalizedApplicationCode),
                    exception.getMessage()
            );
        }
    }

    @Test
    void executeOk() {
        final String givenApplicationCode = "givenApplicationCode";
        final String givenScopeCode = "givenScopeCode";

        final String normalizedApplicationCode = "normalizedApplicationCode";
        final String normalizedScopeCode = "normalizedScopeCode";

        final Application application = Application.builder()
                .code(normalizedApplicationCode)
                .build();

        final OAuth2Scope expectedScope = OAuth2Scope.builder().build();

        when(this.getApplicationUseCase.execute(givenApplicationCode)).thenReturn(application);

        try (MockedStatic<OAuth2ScopeValidationUtils> utilities = mockStatic(OAuth2ScopeValidationUtils.class)) {

            utilities.when(() -> normalizeCode(givenScopeCode)).thenReturn(normalizedScopeCode);

            when(this.oauth2ScopeRepositoryPort.findByApplicationCodeAndCode(
                    normalizedApplicationCode,
                    normalizedScopeCode
            )).thenReturn(Optional.of(expectedScope));

            assertSame(expectedScope, this.subject.execute(
                    givenApplicationCode,
                    givenScopeCode
            ));
        }
    }
}