package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationMapper {

    public static Application toDomain(final ApplicationEntity applicationEntity) {
        return Application.builder()
                .id(applicationEntity.getId())
                .code(applicationEntity.getCode())
                .name(applicationEntity.getName())
                .description(applicationEntity.getDescription())
                .build();
    }

    public static ApplicationEntity toEntity(final Application application) {
        return new ApplicationEntity(
                application.id(),
                application.code(),
                application.name(),
                application.description()
        );
    }
}
