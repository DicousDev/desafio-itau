package br.com.desafioitau.order.repository.postgres;

import br.com.desafioitau.order.filter.PolicyFilter;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.repository.PolicyRepository;
import br.com.desafioitau.order.repository.postgres.jpa.PolicyRepositoryJpa;
import br.com.desafioitau.order.repository.postgres.specification.PolicySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PolicyRepositoryImp implements PolicyRepository {

    private final PolicyRepositoryJpa policyRepositoryJpa;

    public PolicyRepositoryImp(PolicyRepositoryJpa policyRepositoryJpa) {
        this.policyRepositoryJpa = policyRepositoryJpa;
    }

    @Override
    public Optional<Policy> findByIdExternal(UUID idExternal) {
        return policyRepositoryJpa.findByIdExternal(idExternal);
    }

    @Override
    public Optional<Policy> findByIdExternalOrCustomerId(PolicyFilter filter) {
        return policyRepositoryJpa.findOne(PolicySpecification.applyFilter(filter));
    }

    @Override
    public Page<Policy> findAll(PolicyFilter filter) {
        return policyRepositoryJpa.findAll(PageRequest.of(filter.getPage(), filter.getSize()), filter.getPolicySolicitation());
    }

    @Override
    public Policy save(Policy policy) {
        return policyRepositoryJpa.saveAndFlush(policy);
    }

    public List<Policy> saveAll(Collection<Policy> policies) {
        return policyRepositoryJpa.saveAllAndFlush(policies);
    }


}
