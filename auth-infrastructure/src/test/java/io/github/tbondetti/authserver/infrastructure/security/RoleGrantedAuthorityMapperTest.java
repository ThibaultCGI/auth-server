package io.github.tbondetti.authserver.infrastructure.security;

import io.github.tbondetti.authserver.core.domain.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.tbondetti.authserver.infrastructure.security.RoleGrantedAuthorityMapper.toGrantedAuthorities;
import static org.junit.jupiter.api.Assertions.*;

class RoleGrantedAuthorityMapperTest {

    @Test
    void toGrantedAuthoritiesOK() {
        assertEquals(List.of(), toGrantedAuthorities(null));
        assertEquals(List.of(), toGrantedAuthorities(List.of()));

        final Role role1 = Role.builder()
                .codeApplication("11")
                .code("12")
                .build();

        final Role role2 = Role.builder()
                .codeApplication("21")
                .code("22")
                .build();

        final List<AuthServerGrantedAuthority> authorities = toGrantedAuthorities(List.of(role1, role2));

        assertEquals("11.12", authorities.getFirst().getAuthority());
        assertEquals("21.22", authorities.get(1).getAuthority());
    }
}