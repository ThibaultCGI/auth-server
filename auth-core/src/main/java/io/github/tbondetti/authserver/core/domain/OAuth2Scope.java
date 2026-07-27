package io.github.tbondetti.authserver.core.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OAuth2Scope(
        UUID id,
        String applicationCode,
        String code,
        String name,
        String description
) {
    static String COMPLETE_CODE_FORMAT = "%s:%s";

    public String completeCode() {
        return COMPLETE_CODE_FORMAT.formatted(this.applicationCode, this.code);
    }
}