package com.document.generation.app.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonValidator {

    public static JsonNode parseJson(ObjectMapper objectMapper, byte[] jsonData) {
        try {
            return objectMapper.readTree(jsonData);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format", e);
        }
    }
}
