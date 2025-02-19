package com.document.generation.app.service;

import com.document.generation.app.dto.RichTemplateRequest;
import com.document.generation.app.entity.RichTemplate;
import com.document.generation.app.repo.RichTemplateRepo;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RichTemplateServiceImpl implements RichTemplateService {

    private final RichTemplateRepo richTemplateRepo;

    public RichTemplateServiceImpl(RichTemplateRepo richTemplateRepo) {
        this.richTemplateRepo = richTemplateRepo;
    }

    @Override
    public RichTemplate save(RichTemplateRequest request) {
        String decodedContent = extractContentFromHtml(request.content());
        decodedContent = StringEscapeUtils.unescapeHtml4(decodedContent);


        Optional<RichTemplate> byName = richTemplateRepo.findByName(request.name());
        RichTemplate richTemplate = byName.orElseGet(RichTemplate::new);

        richTemplate.setContent(decodedContent);
        richTemplate.setName(request.name());
        richTemplate.setJson(request.json());
        richTemplate.setLocalDateTime(LocalDateTime.now());

        return richTemplateRepo.save(richTemplate);
    }

    @Override
    public Optional<RichTemplate> findById(Long id) {
        return richTemplateRepo.findById(id);
    }

    private String extractContentFromHtml(String input) {
        Document doc = Jsoup.parse(input, "UTF-8");
        return doc.html();
    }
}
