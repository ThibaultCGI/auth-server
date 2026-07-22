package io.github.tbondetti.authserver.infrastructure.service;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.usecase.application.CreateApplicationUseCase;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final CreateApplicationUseCase createApplicationUseCase;
    private final GetApplicationUseCase getApplicationUseCase;

    public Application getApplication( final String code) {
        return this.getApplicationUseCase.execute(code);
    }

    @Transactional
    public Application createApplication(
            final String code,
            final String name,
            final String description
    ) {
        return this.createApplicationUseCase.execute(
                code,
                name,
                description
        );
    }
}
