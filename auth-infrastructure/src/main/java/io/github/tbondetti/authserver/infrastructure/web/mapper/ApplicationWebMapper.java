package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.infrastructure.web.response.ApplicationResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationWebMapper {

    public static ApplicationResponse toResponse(final Application application) {
        return ApplicationResponse
                .builder()
                .id(application.id())
                .code(application.code())
                .name(application.name())
                .description(application.description())
                .build();
    }
}
