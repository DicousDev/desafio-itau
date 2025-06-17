package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.consumer.PaymentReceivedDTO;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.producer.PolicyProducer;
import br.com.desafioitau.order.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdatePaymentPhaseUsecase {

    private final PolicyRepository policyRepository;
    private final PolicyProducer policyProducer;

    @Transactional
    public Policy execute(PaymentReceivedDTO paymentReceived) {
        Optional<Policy> policyOp = policyRepository.findByIdExternal(paymentReceived.getPolicyIdExternal());

        if(policyOp.isEmpty()) {
            return null;
        }

        Policy policy = policyOp.get();
        Boolean isUpdated = switch(paymentReceived.getPaymentPhase()) {
            case CONFIRMED -> policy.confirmPayment();
            case REJECTED -> policy.rejectPayment();
            default -> false;
        };

        if(Boolean.TRUE.equals(isUpdated)) {
            policyProducer.sendPolicy(policy);
        }

        return policyRepository.save(policy);
    }
}
