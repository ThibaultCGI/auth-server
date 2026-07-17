package io.github.tbondetti.authserver.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.github.tbondetti.authserver.infrastructure.constants.TestConstants.PASSWORD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderAdapterTest {

    @InjectMocks
    private PasswordEncoderAdapter subject;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void encodeOk() {
        final String expected = "expected";
        when(this.passwordEncoder.encode(PASSWORD)).thenReturn(expected);

        assertSame(expected, this.subject.encode(PASSWORD));
    }
}