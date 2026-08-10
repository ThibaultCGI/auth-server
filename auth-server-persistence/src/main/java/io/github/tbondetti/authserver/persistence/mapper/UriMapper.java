package io.github.tbondetti.authserver.persistence.mapper;

import lombok.experimental.UtilityClass;

import java.net.URI;

@UtilityClass
public class UriMapper {

    public String toString(final URI uri) {
        return uri == null ? null : uri.toString();
    }


    public URI toDomain(final String value) {
        return value == null ? null : URI.create(value);
    }
}
