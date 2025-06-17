package br.com.desafioitau.order.model;

import br.com.desafioitau.order.exception.EntityInvalidRuntimeException;
import br.com.desafioitau.order.validator.GenericValidator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@EqualsAndHashCode
@Entity
@Table(name = "policy")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID idExternal;

    private UUID customerId;

    private UUID productId;
    @Enumerated(EnumType.STRING)
    private InsuranceCategory category;
    @Enumerated(EnumType.STRING)
    private SalesChannel salesChannel;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentPhase paymentPhase;
    @Enumerated(EnumType.STRING)
    private SubscriptionAuthorizationPhase subscriptionAuthorizationPhase;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal insuredAmount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "policy_id")
    private List<PolicyCoverage> coverages;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "policy_id")
    private List<Assistance> assistances;

    @Enumerated(EnumType.STRING)
    private PolicySolicitation policySolicitation;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "policy_id")
    private List<PolicyHistory> histories;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    public Policy cancel() {
        updateSolicitation(PolicySolicitation.CANCELED);
        return this;
    }

    public Boolean confirmPayment() {

        if(PaymentPhase.IN_PROGRESS.equals(this.paymentPhase)) {
            this.paymentPhase = PaymentPhase.CONFIRMED;

            if(SubscriptionAuthorizationPhase.CONFIRMED.equals(subscriptionAuthorizationPhase)) {
                updateSolicitation(PolicySolicitation.APPROVED);
            }

            return true;
        }

        return false;
    }

    public Boolean rejectPayment() {
        if(PaymentPhase.IN_PROGRESS.equals(this.paymentPhase)) {
            this.paymentPhase = PaymentPhase.REJECTED;
            updateSolicitation(PolicySolicitation.REJECTED);
            return true;
        }

        return false;
    }

    public Boolean confirmSubscriptionAuthorization() {

        if(SubscriptionAuthorizationPhase.IN_PROGRESS.equals(this.subscriptionAuthorizationPhase)) {
            this.subscriptionAuthorizationPhase = SubscriptionAuthorizationPhase.CONFIRMED;

            if(PaymentPhase.CONFIRMED.equals(paymentPhase)) {
                updateSolicitation(PolicySolicitation.APPROVED);
            }

            return true;
        }

        return false;
    }

    public Boolean rejectSubscriptionAuthorization() {

        if(SubscriptionAuthorizationPhase.IN_PROGRESS.equals(this.subscriptionAuthorizationPhase)) {
            this.subscriptionAuthorizationPhase = SubscriptionAuthorizationPhase.REJECTED;
            updateSolicitation(PolicySolicitation.REJECTED);
            return true;
        }

        return false;
    }

    public Policy updateSolicitation(PolicySolicitation policySolicitation) {
        Boolean canNextStatus = this.policySolicitation.getNextSolicitations().contains(policySolicitation);
        if(canNextStatus) {
            histories.add(PolicyHistory.builder()
                .status(this.policySolicitation)
                .createdAt(LocalDateTime.now())
                .build());

            if(PolicySolicitation.APPROVED.equals(policySolicitation) ||
                PolicySolicitation.REJECTED.equals(policySolicitation) ||
                PolicySolicitation.CANCELED.equals(policySolicitation)) {
                finishedAt = LocalDateTime.now();
            }

            this.policySolicitation = policySolicitation;
        }
        else {
            throw new EntityInvalidRuntimeException("Policy solicitation cannot change from [%s] to status [%s]"
                    .formatted(this.policySolicitation, policySolicitation));
        }

        return this;
    }

    public static class PolicyBuilder {

        public Policy build() {

            if(Objects.isNull(idExternal)) {
                idExternal = UUID.randomUUID();
            }

            if(Objects.isNull(createdAt)) {
                createdAt = LocalDateTime.now();
            }

            if(Objects.isNull(paymentPhase)) {
                paymentPhase = PaymentPhase.IN_PROGRESS;
            }

            if(Objects.isNull(subscriptionAuthorizationPhase)) {
                subscriptionAuthorizationPhase = SubscriptionAuthorizationPhase.IN_PROGRESS;
            }

            if(Objects.isNull(id) && Objects.isNull(policySolicitation)) {
                policySolicitation = PolicySolicitation.RECEIVED;
            }

            GenericValidator.validateNotNull(customerId, "Customer cannot be null.");
            GenericValidator.validateNotNull(productId, "Product cannot be null.");
            GenericValidator.validateNotNull(category, "Insurance category cannot be null.");
            GenericValidator.validateNotNull(salesChannel, "Sales channel cannot be null.");
            GenericValidator.validateNotNull(paymentMethod, "Payment method cannot be null.");
            GenericValidator.validateNotNull(totalMonthlyPremiumAmount, "Total monthly premium amount cannot be null.");
            GenericValidator.validateNotNull(insuredAmount, "Insured amount cannot be null.");
            GenericValidator.validateNotNull(policySolicitation, "Policy solicitation cannot be null.");
            GenericValidator.validation("Policy request status cannot be other than [%s].".formatted(PolicySolicitation.RECEIVED), () -> Objects.isNull(id) && !PolicySolicitation.RECEIVED.equals(policySolicitation));
            GenericValidator.validation("It is not possible to create a new policy for this type of payment status [%s].".formatted(paymentPhase), () -> Objects.isNull(id) && !PaymentPhase.IN_PROGRESS.equals(paymentPhase));
            GenericValidator.validation("It is not possible to create a new policy for this type of subscription authorization status [%s].".formatted(subscriptionAuthorizationPhase), () -> Objects.isNull(id) && !SubscriptionAuthorizationPhase.IN_PROGRESS.equals(subscriptionAuthorizationPhase));

            return new Policy(id,
                    idExternal,
                    customerId,
                    productId,
                    category,
                    salesChannel,
                    paymentMethod,
                    paymentPhase,
                    subscriptionAuthorizationPhase,
                    totalMonthlyPremiumAmount,
                    insuredAmount,
                    coverages,
                    assistances,
                    policySolicitation,
                    histories,
                    createdAt,
                    finishedAt);
        }
    }
}
