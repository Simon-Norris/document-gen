package com.document.generation.app.repo;

import com.document.generation.app.entity.RichTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RichTemplateRepo extends JpaRepository<RichTemplate, Long> {
}
