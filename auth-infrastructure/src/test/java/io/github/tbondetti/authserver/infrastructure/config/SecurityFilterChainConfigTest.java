package io.github.tbondetti.authserver.infrastructure.config;

import io.github.tbondetti.authserver.infrastructure.security.ApiAccessDeniedHandler;
import io.github.tbondetti.authserver.infrastructure.security.ApiAuthenticationEntryPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static io.github.tbondetti.authserver.infrastructure.config.SecurityFilterChainConfig.AUTHORIZED_HTTP_REQUESTS_CUSTOMIZER;
import static io.github.tbondetti.authserver.infrastructure.config.SecurityFilterChainConfig.SESSION_MANAGEMENT_CUSTOMIZER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SecurityFilterChainConfigTest {

    @Spy
    @InjectMocks
    private SecurityFilterChainConfig subject;

    @Mock
    private ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;

    @Mock
    private ApiAccessDeniedHandler apiAccessDeniedHandler;

    @Test
    void exceptionHandlingOk() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling = new ExceptionHandlingConfigurer<>();
        this.subject.exceptionHandling().customize(exceptionHandling);


        final Method getAuthenticationEntryPointMethod = ExceptionHandlingConfigurer.class.getDeclaredMethod("getAuthenticationEntryPoint");
        getAuthenticationEntryPointMethod.setAccessible(true);

        final Method getAccessDeniedHandlerMethod = ExceptionHandlingConfigurer.class.getDeclaredMethod("getAccessDeniedHandler");
        getAccessDeniedHandlerMethod.setAccessible(true);

        assertEquals(this.apiAuthenticationEntryPoint, getAuthenticationEntryPointMethod.invoke(exceptionHandling));
        assertEquals(this.apiAccessDeniedHandler, getAccessDeniedHandlerMethod.invoke(exceptionHandling));
    }

    @Test
    void securityFilterChainOk() {
        final Customizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingCustomizer = mock(Customizer.class);
        doReturn(exceptionHandlingCustomizer).when(this.subject).exceptionHandling(); // déjà testé

        final Customizer<HttpBasicConfigurer<HttpSecurity>> httpBasicCustomizer = mock(Customizer.class);
        final SecurityFilterChain securityFilterChain = mock(SecurityFilterChain.class);
        final HttpSecurity httpSecurity = mock(HttpSecurity.class);

        try (final MockedStatic<Customizer> utilities = mockStatic(Customizer.class)) {
            utilities.when(Customizer::withDefaults).thenReturn(httpBasicCustomizer); // déjà testé

            doReturn(httpSecurity).when(httpSecurity).csrf(any());
            doReturn(httpSecurity).when(httpSecurity).sessionManagement(SESSION_MANAGEMENT_CUSTOMIZER);
            doReturn(httpSecurity).when(httpSecurity).authorizeHttpRequests(AUTHORIZED_HTTP_REQUESTS_CUSTOMIZER);
            doReturn(httpSecurity).when(httpSecurity).httpBasic(httpBasicCustomizer);
            doReturn(httpSecurity).when(httpSecurity).exceptionHandling(exceptionHandlingCustomizer);
            doReturn(securityFilterChain).when(httpSecurity).build();

            assertSame(securityFilterChain, this.subject.securityFilterChain(httpSecurity));

            verify(httpSecurity, times(1)).csrf(any());
            verify(httpSecurity, times(1)).sessionManagement(SESSION_MANAGEMENT_CUSTOMIZER);
            verify(httpSecurity, times(1)).authorizeHttpRequests(AUTHORIZED_HTTP_REQUESTS_CUSTOMIZER);
            verify(httpSecurity, times(1)).httpBasic(httpBasicCustomizer);
            verify(httpSecurity, times(1)).exceptionHandling(exceptionHandlingCustomizer);
            verify(httpSecurity, times(1)).build();
        }

    }
}