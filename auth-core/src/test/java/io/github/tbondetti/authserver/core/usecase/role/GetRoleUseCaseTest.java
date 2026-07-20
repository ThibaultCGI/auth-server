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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.usecase.role.GetRoleUseCase.ERROR_ROLE_NOT_FOUND;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRoleUseCaseTest {

    @InjectMocks
    private GetRoleUseCase subject;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;

    @Mock
    private GetApplicationUseCase getApplicationUseCase;

    @Test
    void executeKo() {
        final String givenCode = "givenCode";
        final String givenApplicationCode = "givenApplicationCode";


        try(final MockedStatic<CommonValidationUtils> utilities = mockStatic(CommonValidationUtils.class)) {
            final String normalizedCode = "normalizedCode";
            utilities.when(() -> normalizeCode(givenCode)).thenReturn(normalizedCode);

            final String codeApplication = "codeApplication";
            final Application application = Application.builder().code(codeApplication).build();
            when(this.getApplicationUseCase.execute(givenApplicationCode)).thenReturn(application);

            when(this.roleRepositoryPort.findByCodeAndApplicationCode(normalizedCode, codeApplication)).thenReturn(Optional.empty());

            final AuthServerFunctionalException exception = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> this.subject.execute(givenCode, givenApplicationCode)
            );

            assertEquals(ERROR_ROLE_NOT_FOUND.formatted(normalizedCode, codeApplication), exception.getMessage());
        }

    }

    @Test
    void executeOk() {
        final String givenCode = "givenCode";
        final String givenApplicationCode = "givenApplicationCode";

        try(final MockedStatic<CommonValidationUtils> utilities = mockStatic(CommonValidationUtils.class)) {
            final String normalizedCode = "normalizedCode";
            utilities.when(() -> normalizeCode(givenCode)).thenReturn(normalizedCode);

            final String codeApplication = "codeApplication";
            final Application application = Application.builder().code(codeApplication).build();
            when(this.getApplicationUseCase.execute(givenApplicationCode)).thenReturn(application);

            final Role role = Role.builder().build();
            when(this.roleRepositoryPort.findByCodeAndApplicationCode(normalizedCode, codeApplication)).thenReturn(Optional.of(role));

            assertSame(role, this.subject.execute(givenCode, givenApplicationCode));
        }
    }
}