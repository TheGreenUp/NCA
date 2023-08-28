package ru.green.nca.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonConverter() {

    }

    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
