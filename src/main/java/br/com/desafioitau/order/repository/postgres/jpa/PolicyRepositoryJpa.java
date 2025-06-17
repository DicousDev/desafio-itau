package br.com.desafioitau.order.repository.postgres.jpa;

import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.model.PolicySolicitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PolicyRepositoryJpa extends JpaRepository<Policy, Long>, JpaSpecificationExecutor<Policy> {

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Policy p WHERE p.idExternal=:id")
    Optional<Policy> findByIdExternal(UUID id);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Policy p WHERE p.policySolicitation IN (:solicitations)")
    Page<Policy> findAll(Pageable pageable, @Param("solicitations") List<PolicySolicitation> policySolicitations);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Policy p WHERE p.customerId=:id")
    Optional<Policy> findByCustomerId(UUID id);
}
