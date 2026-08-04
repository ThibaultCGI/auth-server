package io.github.tbondetti.authserver.web.api.v1.mapper;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.web.api.v1.response.ApplicationResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationWebMapper {

    public static ApplicationResponse toResponse(final Application application) {
        return ApplicationResponse
                .builder()
                .code(application.code())
                .name(application.name())
                .description(application.description())
                .build();
    }
}
