package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ScopeEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ScopeMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.OAuth2ScopeJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ScopeMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ScopeMapper.toEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ScopeRepositoryAdapterTest {

    @InjectMocks
    private OAuth2ScopeRepositoryAdapter subject;

    @Mock
    private OAuth2ScopeJpaRepository oauth2ScopeJpaRepository;

    @Mock
    private ApplicationJpaRepository applicationJpaRepository;


    @Test
    void findByApplicationCodeAndCodeOk() {
        final String applicationCode = "applicationCode";
        final String scopeCode = "scopeCode";

        final OAuth2ScopeEntity entity = new OAuth2ScopeEntity();

        when(this.oauth2ScopeJpaRepository.findByApplicationCodeAndCode(
                applicationCode,
                scopeCode
        )).thenReturn(Optional.of(entity));

        try (final MockedStatic<OAuth2ScopeMapper> utilities = mockStatic(OAuth2ScopeMapper.class)) {
            final OAuth2Scope oAuth2Scope = OAuth2Scope.builder().id(UUID.randomUUID()).build();
            utilities.when(() -> toDomain(entity)).thenReturn(oAuth2Scope);

            assertEquals(Optional.of(oAuth2Scope), this.subject.findByApplicationCodeAndCode(applicationCode, scopeCode));
        }
    }

    @Test
    void findByApplicationCodeAndCodeEmpty() {
        final String applicationCode = "applicationCode";
        final String scopeCode = "scopeCode";

        when(this.oauth2ScopeJpaRepository.findByApplicationCodeAndCode(
                applicationCode,
                scopeCode
        )).thenReturn(Optional.empty());

        final Optional<OAuth2Scope> result = this.subject.findByApplicationCodeAndCode(
                applicationCode,
                scopeCode
        );

        assertFalse(result.isPresent());
    }

    @Test
    void saveOk() {
        final String applicationCode = "applicationCode";
        final OAuth2Scope scope = OAuth2Scope.builder().applicationCode(applicationCode).build();

        final String applicationApplicationCode = "applicationApplicationCode";
        final ApplicationEntity application = new ApplicationEntity();
        application.setCode(applicationApplicationCode);
        when(this.applicationJpaRepository.getByCode(applicationCode)).thenReturn(application);

        try (final MockedStatic<OAuth2ScopeMapper> utilities = mockStatic(OAuth2ScopeMapper.class)) {
            final OAuth2ScopeEntity toSave = new OAuth2ScopeEntity();
            utilities.when(() -> toEntity(scope, application)).thenReturn(toSave);

            final String savedApplicationCode = "savedApplicationCode";
            final ApplicationEntity savedApplication = new ApplicationEntity();
            savedApplication.setCode(savedApplicationCode);
            final String savedCode = "savedCode";
            final OAuth2ScopeEntity saved = new OAuth2ScopeEntity();
            saved.setCode(savedCode);
            saved.setApplication(savedApplication);
            when(this.oauth2ScopeJpaRepository.save(toSave)).thenReturn(saved);

            final OAuth2ScopeEntity found = new OAuth2ScopeEntity();
            when(this.oauth2ScopeJpaRepository.getByApplicationCodeAndCode(savedApplicationCode, savedCode)).thenReturn(found);

            final OAuth2Scope expected = OAuth2Scope.builder().build();
            utilities.when(() -> toDomain(found)).thenReturn(expected); // déjà testé

            assertSame(expected, this.subject.save(scope));
        }
    }
}