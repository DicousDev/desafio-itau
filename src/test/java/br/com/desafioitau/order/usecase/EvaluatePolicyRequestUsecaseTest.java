package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.dto.CustomerRatingDTO;
import br.com.desafioitau.order.dto.ValidPolicyRequest;
import br.com.desafioitau.order.filter.PolicyFilter;
import br.com.desafioitau.order.model.CustomerType;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.model.PolicySolicitation;
import br.com.desafioitau.order.producer.PolicyProducer;
import br.com.desafioitau.order.repository.PolicyRepository;
import br.com.desafioitau.order.service.FraudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import support.PolicyProvider;
import support.UUID4T;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class EvaluatePolicyRequestUsecaseTest {

    @Mock
    private PolicyRepository repository;
    @Mock
    private FraudService fraudService;
    @InjectMocks
    private EvaluatePolicyRequestUsecase evaluatePolicyRequestUsecase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldProcessPoliciesReceived() {
        Page<Policy> policiesPage = new PageImpl<>(List.of(PolicyProvider.create().policySolicitation(PolicySolicitation.RECEIVED).build()), PageRequest.of(0, 10), 1);
        Mockito.when(repository.findAll(Mockito.any(PolicyFilter.class))).thenReturn(policiesPage);
        Mockito.when(fraudService.validPolicy(Mockito.any(ValidPolicyRequest.class))).thenReturn(CustomerRatingDTO.builder()
            .customerId(UUID4T.UUID_0)
            .policyId(UUID4T.UUID_0)
            .analyzedAt(LocalDateTime.now())
            .classification(CustomerType.HIGH_RISK)
            .occurrences(Collections.emptyList())
            .build());

        evaluatePolicyRequestUsecase.execute();
        Mockito.verify(repository).saveAll(Mockito.anyList());
    }
}
