package com.document.generation.app.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonValidator {
    public static boolean isValidJson(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(file);
            return jsonNode != null;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        File jsonFile = new File("data.json");
        if (isValidJson(jsonFile)) {
            System.out.println("Valid JSON ✅");
        } else {
            System.out.println("Invalid JSON ❌");
        }
    }
}
