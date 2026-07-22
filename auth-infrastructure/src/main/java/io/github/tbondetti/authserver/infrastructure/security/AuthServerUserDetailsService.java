package io.github.tbondetti.authserver.infrastructure.security;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.usecase.user.GetAllUserRolesUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static io.github.tbondetti.authserver.infrastructure.security.ApiAuthenticationEntryPoint.ERROR_INVALID_CREDENTIALS;
import static io.github.tbondetti.authserver.infrastructure.security.RoleGrantedAuthorityMapper.toGrantedAuthorities;

@RequiredArgsConstructor
public class AuthServerUserDetailsService implements UserDetailsService {

    private final GetUserUseCase getUserUseCase;
    private final GetAllUserRolesUseCase getAllUserRolesUseCase;

    @Nonnull
    @Override
    public UserDetails loadUserByUsername(@Nonnull final String username) {
        final User user = this.getUser(username);
        final List<Role> userRoles = this.getAllUserRolesUseCase.execute(user.username());

        final List<AuthServerGrantedAuthority> authorities = toGrantedAuthorities(userRoles);

        return new AuthServerUserDetails(user, authorities);
    }

    @SuppressWarnings("java:S5804") // aucune information remontée à l'utilisateur
    protected User getUser(final String username) {
        try {
            return this.getUserUseCase.execute(username);
        } catch (final AuthServerFunctionalException e) {
            throw new UsernameNotFoundException(ERROR_INVALID_CREDENTIALS, e);
        }
    }
}
