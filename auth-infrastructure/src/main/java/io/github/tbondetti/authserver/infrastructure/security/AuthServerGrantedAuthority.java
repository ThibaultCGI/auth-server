package io.github.tbondetti.authserver.infrastructure.security;

import io.github.tbondetti.authserver.core.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class AuthServerGrantedAuthority implements GrantedAuthority {

    private final String authority;

    public AuthServerGrantedAuthority(final Role role) {
        this.authority = role.codeApplication() + "." + role.code();
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
