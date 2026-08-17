package io.github.tbondetti.authserver.security.properties;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JwtKeyStorePropertiesTest {

    @Test
    void isActiveOk() {
        final JwtKeyStoreProperties properties = new JwtKeyStoreProperties(
                "location",
                "storePassword",
                new HashMap<>() {{
                    put("key1", "value1");
                    put("key2", "value2");

                }} ,
                "key2"
        );

        assertTrue(properties.isActive("key2"));
        assertFalse(properties.isActive("key1"));
    }
}