package br.com.desafioitau.order.repository;

import br.com.desafioitau.order.filter.PolicyFilter;
import br.com.desafioitau.order.model.Policy;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PolicyRepository {

    Optional<Policy> findByIdExternal(UUID idExternal);
    Optional<Policy> findByIdExternalOrCustomerId(PolicyFilter filter);
    Page<Policy> findAll(PolicyFilter filter);
    Policy save(Policy policy);
    List<Policy> saveAll(Collection<Policy> policies);
}
