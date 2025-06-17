package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.dto.CustomerRatingDTO;
import br.com.desafioitau.order.dto.ValidPolicyRequest;
import br.com.desafioitau.order.filter.PolicyFilter;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.model.PolicySolicitation;
import br.com.desafioitau.order.repository.PolicyRepository;
import br.com.desafioitau.order.service.FraudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluatePolicyRequestUsecase {

    private final PolicyRepository repository;
    private final FraudService fraudService;

    @Transactional
    public void execute() {
        int pageCurrent = 0;
        int size = 10;
        boolean isNextPage = true;
        do {
            PolicyFilter filter = PolicyFilter.builder()
                    .policySolicitation(List.of(PolicySolicitation.RECEIVED))
                    .page(pageCurrent)
                    .size(size)
                    .build();

            Page<Policy> policiesPage = repository.findAll(filter);
            processPolicies(policiesPage.getContent());

            isNextPage = policiesPage.hasNext();
            pageCurrent++;
        }while(isNextPage);
    }

    private void processPolicies(Collection<Policy> policies) {

        List<Policy> policiesUpdated = new ArrayList<>();
        for(Policy policy : policies) {
            CustomerRatingDTO customerRating = fraudService.validPolicy(ValidPolicyRequest.builder()
                    .customerId(policy.getCustomerId())
                    .policyId(policy.getIdExternal())
                    .build());
            PolicySolicitation policySolicitation = customerRating.getClassification().analysePolicy(policy);
            policy.updateSolicitation(policySolicitation);
            policiesUpdated.add(policy);
        }

        repository.saveAll(policiesUpdated);
    }
}
