package br.com.desafioitau.order.model;

import br.com.desafioitau.order.validator.GenericValidator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "policy_coverage")
public class PolicyCoverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private BigDecimal amount;

    public static class PolicyCoverageBuilder {

        public PolicyCoverage build() {

            GenericValidator.validateNotNull(title, "Title cannot be null.");
            GenericValidator.validateMaxLength(title, 255, "Title exceeded character limit");
            GenericValidator.validateNotNull(amount, "Amount cannot be null.");
            return new PolicyCoverage(id, title, amount);
        }
    }
}
