package br.com.desafioitau.order.model;

import br.com.desafioitau.order.validator.GenericValidator;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "policy_history")
public class PolicyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PolicySolicitation status;
    private LocalDateTime createdAt;

    public static class PolicyHistoryBuilder {

        public PolicyHistory build() {

            GenericValidator.validateNotNull(status, "Status cannot be null.");
            GenericValidator.validateNotNull(createdAt, "Created at cannot be null.");

            return new PolicyHistory(id, status, createdAt);
        }
    }
}
