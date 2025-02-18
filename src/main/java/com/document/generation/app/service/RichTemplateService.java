package com.document.generation.app.service;

import com.document.generation.app.dto.RichTemplateRequest;
import com.document.generation.app.entity.RichTemplate;

import java.util.Optional;

public interface RichTemplateService {

    RichTemplate save(RichTemplateRequest richTemplate);

    Optional<RichTemplate> findById(Long id);
}
