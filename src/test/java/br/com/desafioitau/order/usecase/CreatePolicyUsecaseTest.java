package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.controller.policy.request.CreatePolicyRequest;
import br.com.desafioitau.order.controller.policy.response.CreatePolicyResponse;
import br.com.desafioitau.order.model.InsuranceCategory;
import br.com.desafioitau.order.model.PaymentMethod;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.model.SalesChannel;
import br.com.desafioitau.order.producer.PolicyProducer;
import br.com.desafioitau.order.repository.PolicyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import support.PolicyProvider;
import support.UUID4T;

import java.math.BigDecimal;
import java.util.Collections;

public class CreatePolicyUsecaseTest {

    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private PolicyProducer policyProducer;

    @InjectMocks
    private CreatePolicyUsecase createPolicyUsecase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createPolicy() {
        CreatePolicyRequest request = CreatePolicyRequest.builder()
                .customerId(UUID4T.UUID_0)
                .productId(UUID4T.UUID_0)
                .category(InsuranceCategory.AUTO)
                .salesChannel(SalesChannel.WHATSAPP)
                .paymentMethod(PaymentMethod.PIX)
                .totalMonthlyPremiumAmount(BigDecimal.valueOf(80000))
                .insuredAmount(BigDecimal.valueOf(75000))
                .coverages(Collections.emptyList())
                .assistances(Collections.emptyList())
                .build();

        Policy policy = PolicyProvider.create().build();
        Mockito.when(policyRepository.save(Mockito.any(Policy.class))).thenReturn(policy);
        CreatePolicyResponse response = createPolicyUsecase.execute(request);

        Assertions.assertThat(response).hasNoNullFieldsOrProperties();
        Mockito.verify(policyRepository).save(Mockito.any(Policy.class));
        Mockito.verify(policyProducer).sendPolicy(policy);
    }
}
