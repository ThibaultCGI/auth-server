package io.github.tbondetti.authserver.infrastructure.security;

import io.github.tbondetti.authserver.core.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthServerUserDetails implements UserDetails {

    private final String username;
    private final String passwordHash;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthServerUserDetails(final User user,
                                 final List<AuthServerGrantedAuthority> authorities) {
        this.username = user.username();
        this.passwordHash = user.passwordHash();
        this.enabled = user.enabled();
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
