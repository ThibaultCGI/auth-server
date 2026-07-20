package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.ApplicationMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.constants.TestConstants.APPLICATION_CODE;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.ApplicationMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.ApplicationMapper.toEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationRepositoryAdapterTest {

    @InjectMocks
    private ApplicationRepositoryAdapter subject;

    @Mock
    private ApplicationJpaRepository applicationJpaRepository;

    @Test
    void findByCodeOk() {
        final ApplicationEntity applicationEntity = new ApplicationEntity();
        final Application expected = Application.builder().build();

        when(this.applicationJpaRepository.findByCode(APPLICATION_CODE)).thenReturn(Optional.of(applicationEntity));

        try (final MockedStatic<ApplicationMapper> utilities = mockStatic(ApplicationMapper.class)) {
            utilities.when(() -> toDomain(applicationEntity)).thenReturn(expected); // déjà testé

            assertEquals(Optional.of(expected), this.subject.findByCode(APPLICATION_CODE));
        }
    }

    @Test
    void saveOk() {
        final Application given = Application.builder().build();

        final ApplicationEntity mapped = new ApplicationEntity();
        final ApplicationEntity saved = new ApplicationEntity();

        final Application expected = Application.builder().build();

        try (final MockedStatic<ApplicationMapper> utilities = mockStatic(ApplicationMapper.class)) {
            utilities.when(() -> toEntity(given)).thenReturn(mapped); // déjà testé

            when(this.applicationJpaRepository.save(mapped)).thenReturn(saved);

            utilities.when(() -> toDomain(saved)).thenReturn(expected); // déjà testé

            assertSame(expected, this.subject.save(given));
        }
    }
}