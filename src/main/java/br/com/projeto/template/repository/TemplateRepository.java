package br.com.projeto.template.repository;

import br.com.projeto.template.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}
