package integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import support.ITemplate;
import support.ResponseEntityAssert;

import java.net.URI;

public class TemplateControllerTest extends ITemplate {

    private static final String BASE_URL = "pessoas";

    @Disabled("Não é um teste válido e sim de exemplo.")
    @Test
    @Sql({"classpath:IT/clean.sql"})
    void deveCadastrarTemplate() {
        String requestBody = readJSON("IT/template-request.json");
        String expected = readJSON("IT/template-response.json");
        URI uri = toURI(BASE_URL);
        ResponseEntity<String> response = restTemplate.sendPOST(uri, requestBody);
        ResponseEntityAssert.assertThat(response)
                .isCreated()
                .responseBody(expected);
    }
}
