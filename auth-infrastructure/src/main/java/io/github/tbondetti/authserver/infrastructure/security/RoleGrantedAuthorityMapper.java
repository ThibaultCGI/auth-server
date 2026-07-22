package io.github.tbondetti.authserver.infrastructure.security;

import io.github.tbondetti.authserver.core.domain.Role;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.Objects.isNull;

@UtilityClass
public class RoleGrantedAuthorityMapper {

    public static AuthServerGrantedAuthority toGrantedAuthority(final Role role) {
        return new AuthServerGrantedAuthority(role);
    }

    public static List<AuthServerGrantedAuthority> toGrantedAuthorities(final List<Role> roles) {
        if (isNull(roles) || roles.isEmpty()) {
            return List.of();
        }

        return roles.stream().map(RoleGrantedAuthorityMapper::toGrantedAuthority).toList();
    }

}
