package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.consumer.SubscriptionAuthorizationReceivedDTO;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.producer.PolicyProducer;
import br.com.desafioitau.order.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateSubscriptionAuthorizationPhaseUsecase {

    private final PolicyRepository policyRepository;
    private final PolicyProducer policyProducer;

    @Transactional
    public Policy execute(SubscriptionAuthorizationReceivedDTO subscriptionAuthorizationReceived) {
        Optional<Policy> policyOp = policyRepository.findByIdExternal(subscriptionAuthorizationReceived.getPolicyIdExternal());

        if(policyOp.isEmpty()) {
            return null;
        }

        Policy policy = policyOp.get();
        Boolean isUpdated = switch(subscriptionAuthorizationReceived.getSubscriptionAuthorizationPhase()) {
            case CONFIRMED -> policy.confirmSubscriptionAuthorization();
            case REJECTED -> policy.rejectSubscriptionAuthorization();
            default -> false;
        };

        if(Boolean.TRUE.equals(isUpdated)) {
            policyProducer.sendPolicy(policy);
        }

        return policyRepository.save(policy);
    }
}
