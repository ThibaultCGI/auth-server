package io.github.tbondetti.authserver.core.usecase.user;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.core.utils.UserValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.constants.TestConstants.USER_NAME;
import static io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase.ERROR_USER_NOT_FOUND;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.normalizeUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {

    @InjectMocks
    private GetUserUseCase subject;

    @Mock
    private UserRepositoryPort userRepositoryPort;


    @Test
    void executeKo() {
        final String normalizedUsername = "normalizedUsername";

        try(final MockedStatic<UserValidationUtils> userUtilities = mockStatic(UserValidationUtils.class)) {
            userUtilities.when(() -> normalizeUsername(USER_NAME)).thenReturn(normalizedUsername); // déjà testé
            when(this.userRepositoryPort.findByUsername(normalizedUsername)).thenReturn(Optional.empty());

            final AuthServerFunctionalException exception = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> this.subject.execute(USER_NAME)
            );

            assertEquals(ERROR_USER_NOT_FOUND.formatted(normalizedUsername), exception.getMessage());
        }
    }

    @Test
    void executeOk() {
        final String normalizedUsername = "normalizedUsername";
        final User expectedUser = User.builder().build();

        try(final MockedStatic<UserValidationUtils> userUtilities = mockStatic(UserValidationUtils.class)) {
            userUtilities.when(() -> normalizeUsername(USER_NAME)).thenReturn(normalizedUsername); // déjà testé
            when(this.userRepositoryPort.findByUsername(normalizedUsername)).thenReturn(Optional.of(expectedUser));

            assertSame(expectedUser, this.subject.execute(USER_NAME));
        }
    }

}