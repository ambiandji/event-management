package org.crok4it.em.unit.resource;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class BaseResourceTest {
    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
