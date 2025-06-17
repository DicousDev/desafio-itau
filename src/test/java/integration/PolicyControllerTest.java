package integration;

import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.model.PolicySolicitation;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;
import support.ITemplate;
import support.ResponseEntityAssert;
import support.UUID4T;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class PolicyControllerTest extends ITemplate {

    private static final String BASE_URL = "policies";

    @Autowired
    private EntityManager entityManager;

    private ClientAndServer clientAndServer;

    @Test
    @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-policies.sql"})
    void shouldFindPolicyByIdExternal() {
        String expected = readJSON("IT/shouldFindPolicyByIdExternalExpected.json");
        URI uri = UriComponentsBuilder.fromUri(toURI(BASE_URL))
                .queryParam("idExternal", UUID4T.UUID_0)
                .build()
                .toUri();
        ResponseEntity<String> response = restTemplate.sendGET(uri);
        ResponseEntityAssert.assertThat(response)
                .isOk()
                .responseBody(expected);
    }

    @Test
    @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-policies.sql"})
    void shouldFailFindPolicyByIdExternalNotFound() {
        String expected = readJSON("IT/shouldFailFindPolicyByIdExternalNotFoundExpected.json");
        URI uri = UriComponentsBuilder.fromUri(toURI(BASE_URL))
                .queryParam("idExternal", UUID.fromString("e1ffebd4-a9bd-4e76-9a87-85d6771d2b48"))
                .build()
                .toUri();
        ResponseEntity<String> response = restTemplate.sendGET(uri);
        ResponseEntityAssert.assertThat(response)
                .isNotFound()
                .responseBody(expected, "timestamp");
    }

    @Test
    @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-policies.sql"})
    void shouldFindPolicyByCustomerId() {
        String expected = readJSON("IT/shouldFindPolicyByCustomerIdExpected.json");
        URI uri = UriComponentsBuilder.fromUri(toURI(BASE_URL))
                .queryParam("customerId", UUID4T.UUID_0)
                .build()
                .toUri();
        ResponseEntity<String> response = restTemplate.sendGET(uri);
        ResponseEntityAssert.assertThat(response)
                .isOk()
                .responseBody(expected);
    }

    @Test
    @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-policies.sql"})
    void shouldFailFindPolicyByCustomerIdNotFound() {
        String expected = readJSON("IT/shouldFailFindPolicyByCustomerIdNotFoundExpected.json");
        URI uri = UriComponentsBuilder.fromUri(toURI(BASE_URL))
                .queryParam("idExternal", UUID.fromString("e1ffebd4-a9bd-4e76-9a87-85d6771d2b48"))
                .build()
                .toUri();
        ResponseEntity<String> response = restTemplate.sendGET(uri);
        ResponseEntityAssert.assertThat(response)
                .isNotFound()
                .responseBody(expected, "timestamp");
    }

    @Test
    @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-policies.sql"})
    void shouldFailFindPolicyWithoutParam() {
        String expected = readJSON("IT/shouldFailFindPolicyWithoutParamResponse.json");
        URI uri = toURI(BASE_URL);
        ResponseEntity<String> response = restTemplate.sendGET(uri);
        ResponseEntityAssert.assertThat(response)
                .isUnprocessableEntity()
                .responseBody(expected, "timestamp");
    }

    @Test
    @Sql({"classpath:IT/clean.sql"})
    void shouldRegisterPolicy() {
        String fraudResponse = readJSON("IT/fraud-response.json");
        clientAndServer = startClientAndServer(1080);
        clientAndServer.when(HttpRequest.request().withContentType(MediaType.APPLICATION_JSON).withMethod("POST").withPath("/valid"))
                .respond(HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(fraudResponse).withStatusCode(200));

        String requestBody = readJSON("IT/create-policy-request.json");
        URI uri = toURI(BASE_URL);
        ResponseEntity<String> response = restTemplate.sendPOST(uri, requestBody);
        ResponseEntityAssert.assertThat(response).isCreated();

        List<Object> results = entityManager.createQuery("SELECT p FROM Policy p").getResultList();
        Assertions.assertThat(results).hasSize(1);
    }

    @Test
    @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-policies.sql"})
    void shouldCancelPolicy() {
        URI uri = toURI(BASE_URL + "/00000000-0000-0000-0000-000000000000");
        ResponseEntity<String> response = restTemplate.sendPATCH(uri);
        ResponseEntityAssert.assertThat(response).isOk();

        Policy policy = entityManager.createQuery("SELECT p FROM Policy p WHERE p.idExternal=:id", Policy.class)
                .setParameter("id", UUID4T.UUID_0)
                .getSingleResult();

        Assertions.assertThat(policy.getPolicySolicitation()).isEqualTo(PolicySolicitation.CANCELED);
        Assertions.assertThat(policy.getFinishedAt()).isNotNull();
    }

    @Test
    @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-policies.sql"})
    void shouldFailCancelPolicyNotFound() {
        String expected = readJSON("IT/shouldFailCancelPolicyNotFoundResponse.json");
        URI uri = toURI(BASE_URL + "/e1ffebd4-a9bd-4e76-9a87-85d6771d2b48");
        ResponseEntity<String> response = restTemplate.sendPATCH(uri);
        ResponseEntityAssert.assertThat(response)
                .isNotFound()
                .responseBody(expected, "timestamp");
    }
}
