package br.com.projeto.template.service;

import br.com.projeto.template.converter.TemplateConverter;
import br.com.projeto.template.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TemplateServiceTest {

    @Mock
    private TemplateRepository repository;
    private TemplateService service;
    private TemplateConverter converter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new TemplateService(repository, new TemplateConverter());
    }
}
