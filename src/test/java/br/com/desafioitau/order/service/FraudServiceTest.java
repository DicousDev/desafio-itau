package br.com.desafioitau.order.service;

import br.com.desafioitau.order.dto.ValidPolicyRequest;
import br.com.desafioitau.order.feign.FraudFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import support.UUID4T;

public class FraudServiceTest {

    @InjectMocks
    private FraudService fraudService;
    @Mock
    private FraudFeignClient fraudFeignClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCallFeign() {
        ValidPolicyRequest request = ValidPolicyRequest.builder()
                .policyId(UUID4T.UUID_0)
                .customerId(UUID4T.UUID_0)
                .build();

        fraudService.validPolicy(request);
        Mockito.verify(fraudFeignClient).validPolicy(request);
    }
}
