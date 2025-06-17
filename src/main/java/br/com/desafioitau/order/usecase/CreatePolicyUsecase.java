package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.controller.policy.request.CreatePolicyRequest;
import br.com.desafioitau.order.controller.policy.request.PolicyCoverageRequest;
import br.com.desafioitau.order.controller.policy.response.CreatePolicyResponse;
import br.com.desafioitau.order.model.Assistance;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.model.PolicyCoverage;
import br.com.desafioitau.order.model.PolicySolicitation;
import br.com.desafioitau.order.producer.PolicyProducer;
import br.com.desafioitau.order.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePolicyUsecase {

    private final PolicyRepository policyRepository;
    private final PolicyProducer policyProducer;

    @Transactional
    public CreatePolicyResponse execute(CreatePolicyRequest input) {
        Policy policy = toPolicy(input);
        policy = policyRepository.save(policy);

        policyProducer.sendPolicy(policy);

        return CreatePolicyResponse.builder()
                .policyId(policy.getIdExternal())
                .timestamp(policy.getCreatedAt())
                .build();
    }

    private Policy toPolicy(CreatePolicyRequest input) {
        return Policy.builder()
                .policySolicitation(PolicySolicitation.RECEIVED)
                .customerId(input.getCustomerId())
                .productId(input.getProductId())
                .category(input.getCategory())
                .salesChannel(input.getSalesChannel())
                .paymentMethod(input.getPaymentMethod())
                .totalMonthlyPremiumAmount(input.getTotalMonthlyPremiumAmount())
                .insuredAmount(input.getInsuredAmount())
                .coverages(input.getCoverages().stream().map(this::toPolicyCoverage).toList())
                .assistances(input.getAssistances().stream().map(this::toAssistance).toList())
                .build();
    }

    private PolicyCoverage toPolicyCoverage(PolicyCoverageRequest coverage) {
        return PolicyCoverage.builder()
                .title(coverage.getTitle())
                .amount(coverage.getAmount())
                .build();
    }

    private Assistance toAssistance(String s) {
        return Assistance.builder()
                .assistance(s)
                .build();
    }
}
