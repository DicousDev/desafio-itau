package br.com.projeto.template.service;

import br.com.projeto.template.converter.TemplateConverter;
import br.com.projeto.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository repository;
    private final TemplateConverter converter;

    public void execute() {

    }
}
