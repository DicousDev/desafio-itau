package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.controller.policy.response.GetHistoryResponse;
import br.com.desafioitau.order.controller.policy.response.GetPolicyResponse;
import br.com.desafioitau.order.exception.EntityNotFoundRuntimeException;
import br.com.desafioitau.order.filter.PolicyFilter;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FindPolicyByIdExternalOrCustomerIdUsecase {

    private final PolicyRepository repository;

    @Transactional(readOnly = true)
    public GetPolicyResponse execute(PolicyFilter filter) {
        Policy policy = repository.findByIdExternalOrCustomerId(filter)
                .orElseThrow(() -> new EntityNotFoundRuntimeException("Policy not found."));

        return GetPolicyResponse.builder()
                .id(policy.getIdExternal())
                .customerId(policy.getCustomerId())
                .productId(policy.getProductId())
                .category(policy.getCategory())
                .salesChannel(policy.getSalesChannel())
                .paymentMethod(policy.getPaymentMethod())
                .status(policy.getPolicySolicitation())
                .createdAt(policy.getCreatedAt())
                .finishedAt(policy.getFinishedAt())
                .totalMonthlyPremiumAmount(policy.getTotalMonthlyPremiumAmount())
                .insuredAmount(policy.getInsuredAmount())
                .assistances(getAssistances(policy))
                .history(getHistories(policy))
                .build();
    }

    private List<String> getAssistances(Policy policy) {

        if(Objects.isNull(policy.getAssistances()) || policy.getAssistances().isEmpty()) {
            return Collections.emptyList();
        }

        return policy.getAssistances().stream()
                .map(assistance -> assistance.getAssistance())
                .toList();
    }

    private List<GetHistoryResponse> getHistories(Policy policy) {

        if(Objects.isNull(policy.getHistories()) || policy.getHistories().isEmpty()) {
            return Collections.emptyList();
        }

        return policy.getHistories().stream()
                .map(history -> GetHistoryResponse.builder()
                        .status(history.getStatus())
                        .timestamp(history.getCreatedAt())
                        .build())
                .toList();
    }
}
