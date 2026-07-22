package io.github.tbondetti.authserver.infrastructure.security;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.usecase.user.GetAllUserRolesUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.USER_NOT_FOUND;
import static io.github.tbondetti.authserver.infrastructure.security.ApiAuthenticationEntryPoint.ERROR_INVALID_CREDENTIALS;
import static io.github.tbondetti.authserver.infrastructure.security.RoleGrantedAuthorityMapper.toGrantedAuthorities;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServerUserDetailsServiceTest {

    @Spy
    @InjectMocks
    private AuthServerUserDetailsService subject;

    @Mock
    private GetUserUseCase getUserUseCase;

    @Mock
    private GetAllUserRolesUseCase getAllUserRolesUseCase;

    @Test
    void getUserKo() {
        final String givenUsername = "givenUsername";

        final AuthServerFunctionalException exception = new AuthServerFunctionalException(USER_NOT_FOUND, "Utilisateur introuvable");
        when(this.getUserUseCase.execute(givenUsername)).thenThrow(exception);

        final UsernameNotFoundException actual = assertThrows(
                UsernameNotFoundException.class,
                () -> this.subject.getUser(givenUsername)
        );

        assertSame(ERROR_INVALID_CREDENTIALS, actual.getMessage());
        assertSame(exception, actual.getCause());
    }

    @Test
    void getUserOk() {
        final String givenUsername = "givenUsername";

        final User user = User.builder().build();
        when(this.getUserUseCase.execute(givenUsername)).thenReturn(user);

        assertSame(user, this.subject.getUser(givenUsername));
    }

    @Test
    void loadUserByUsernameOk() {
        final String givenUsername = "givenUsername";

        final String username = "username";
        final String passwordHash = "passwordHash";
        final User user = User.builder()
                .username(username)
                .passwordHash(passwordHash)
                .enabled(true)
                .build();
        doReturn(user).when(this.subject).getUser(givenUsername); // déjà testé

        final List<Role> userRoles = List.of();
        when(this.getAllUserRolesUseCase.execute(username)).thenReturn(userRoles);

        final List<AuthServerGrantedAuthority> authorities = List.of();
        try (final MockedStatic<RoleGrantedAuthorityMapper> utilities = mockStatic(RoleGrantedAuthorityMapper.class)) {
            utilities.when(() -> toGrantedAuthorities(userRoles)).thenReturn(authorities); // déjà testé

            final UserDetails userDetails = this.subject.loadUserByUsername(givenUsername);

            assertSame(username, userDetails.getUsername());
            assertSame(passwordHash, userDetails.getPassword());
            assertTrue(userDetails.isEnabled());
            assertSame(authorities, userDetails.getAuthorities());
        }
    }
}