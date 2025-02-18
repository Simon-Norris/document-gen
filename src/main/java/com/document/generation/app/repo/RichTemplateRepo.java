package com.document.generation.app.repo;

import com.document.generation.app.entity.RichTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RichTemplateRepo extends JpaRepository<RichTemplate, Long> {

    Optional<RichTemplate> findByName(String name);
}
