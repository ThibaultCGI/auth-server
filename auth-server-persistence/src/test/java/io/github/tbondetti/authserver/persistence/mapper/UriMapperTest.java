package io.github.tbondetti.authserver.persistence.mapper;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class UriMapperTest {

    @Test
    void toStringOk() {
        assertNull(UriMapper.toString(null));
        assertEquals("abc", UriMapper.toString(URI.create("abc")));
    }

    @Test
    void toDomainOk(){
        assertNull(UriMapper.toDomain(null));
        assertEquals(URI.create("abc"), UriMapper.toDomain("abc"));
    }
}