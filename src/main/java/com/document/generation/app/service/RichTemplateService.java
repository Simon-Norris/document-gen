package com.document.generation.app.service;

import com.document.generation.app.entity.RichTemplate;

import java.util.Optional;

public interface RichTemplateService {

    RichTemplate save(RichTemplate richTemplate);

    Optional<RichTemplate> findById(Long id);
}
