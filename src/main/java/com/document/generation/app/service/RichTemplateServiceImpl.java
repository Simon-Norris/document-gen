package com.document.generation.app.service;

import com.document.generation.app.entity.RichTemplate;
import com.document.generation.app.repo.RichTemplateRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RichTemplateServiceImpl implements RichTemplateService {

    private final RichTemplateRepo richTemplateRepo;

    public RichTemplateServiceImpl(RichTemplateRepo richTemplateRepo) {
        this.richTemplateRepo = richTemplateRepo;
    }

    @Override
    public RichTemplate save(RichTemplate richTemplate) {
        return richTemplateRepo.save(richTemplate);
    }

    @Override
    public Optional<RichTemplate> findById(Long id) {
        return richTemplateRepo.findById(id);
    }
}
