package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.exception.EntityNotFoundRuntimeException;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelPolicyUsecase {

    private final PolicyRepository repository;

    @Transactional
    public void execute(UUID idExternal) {
        Policy policy = repository.findByIdExternal(idExternal)
                .orElseThrow(() -> new EntityNotFoundRuntimeException("Policy not found."));

        repository.save(policy.cancel());
    }
}
