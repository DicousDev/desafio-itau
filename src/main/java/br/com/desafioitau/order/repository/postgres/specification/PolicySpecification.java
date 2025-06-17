package br.com.desafioitau.order.repository.postgres.specification;

import br.com.desafioitau.order.filter.PolicyFilter;
import br.com.desafioitau.order.model.Policy;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class PolicySpecification {

    public static Specification<Policy> applyFilter(PolicyFilter filter) {
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if(Objects.nonNull(filter.getIdExternal())) {
                predicate = builder.and(predicate, builder.equal(root.get("idExternal"), filter.getIdExternal()));
            }

            if(Objects.nonNull(filter.getCustomerId())) {
                predicate = builder.and(predicate, builder.equal(root.get("customerId"), filter.getCustomerId()));
            }

            return predicate;
        };
    }
}
