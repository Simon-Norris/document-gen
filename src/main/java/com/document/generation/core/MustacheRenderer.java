package com.document.generation.core;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Service
public class MustacheRenderer implements DocumentRenderer {
    private final ObjectMapper objectMapper;

    public MustacheRenderer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T, R> R render(T template, Object... args) {
        if (!(template instanceof String templateString)) {
            throw new IllegalArgumentException("Mustache template must be a string.");
        }

        Map<String, Object> context = objectMapper.convertValue(args[0], Map.class);

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(templateString), "template");

        StringWriter writer = new StringWriter();
        mustache.execute(writer, context);

        return (R) writer.toString();
    }
}
