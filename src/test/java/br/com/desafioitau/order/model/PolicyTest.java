package br.com.desafioitau.order.model;

import br.com.desafioitau.order.exception.EntityInvalidRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import support.PolicyProvider;

import java.util.stream.Stream;

public class PolicyTest {

    @Test
    public void shouldCancelPolicy() {
        Policy policy = PolicyProvider.create()
                .policySolicitation(PolicySolicitation.PENDING)
                .build();

        policy.cancel();
        Assertions.assertThat(policy.getPolicySolicitation()).isEqualTo(PolicySolicitation.CANCELED);
        Assertions.assertThat(policy.getFinishedAt()).isNotNull();
    }

    @Test
    public void shouldSetFinishedAtWhenUpdatePolicySolicitationToApproved() {
        Policy policy = PolicyProvider.create()
                .policySolicitation(PolicySolicitation.PENDING)
                .build();

        policy.updateSolicitation(PolicySolicitation.APPROVED);
        Assertions.assertThat(policy.getFinishedAt()).isNotNull();
    }

    @Test
    public void shouldSetFinishedAtWhenUpdatePolicySolicitationToRejected() {
        Policy policy = PolicyProvider.create()
                .policySolicitation(PolicySolicitation.PENDING)
                .build();

        policy.updateSolicitation(PolicySolicitation.REJECTED);
        Assertions.assertThat(policy.getFinishedAt()).isNotNull();
    }

    @Test
    public void shouldSetFinishedAtWhenUpdatePolicySolicitationToCanceled() {
        Policy policy = PolicyProvider.create()
                .policySolicitation(PolicySolicitation.PENDING)
                .build();

        policy.updateSolicitation(PolicySolicitation.CANCELED);
        Assertions.assertThat(policy.getFinishedAt()).isNotNull();
    }

    @Test
    public void shouldNotUpdatePaymentPhaseWhenItHasAlreadyBeenConfirmed() {
        Policy policy = PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING)
                .paymentPhase(PaymentPhase.CONFIRMED)
                .build();

        Boolean confirmedPayment = policy.confirmPayment();
        Assertions.assertThat(confirmedPayment).isFalse();
    }

    @Test
    public void shouldNotUpdatePaymentPhaseWhenItHasAlreadyBeenRejected() {
        Policy policy = PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING)
                .paymentPhase(PaymentPhase.REJECTED)
                .build();

        Boolean confirmedPayment = policy.confirmPayment();
        Assertions.assertThat(confirmedPayment).isFalse();
    }

    @Test
    public void shouldNotUpdateSubscriptionAuthorizationPhaseWhenItHasAlreadyBeenConfirmed() {
        Policy policy = PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.CONFIRMED)
                .build();

        Boolean confirm = policy.confirmSubscriptionAuthorization();
        Assertions.assertThat(confirm).isFalse();
    }

    @Test
    public void shouldNotUpdateSubscriptionAuthorizationPhaseWhenItHasAlreadyBeenRejected() {
        Policy policy = PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.REJECTED)
                .build();

        Boolean confirm = policy.confirmSubscriptionAuthorization();
        Assertions.assertThat(confirm).isFalse();
    }

    @Test
    public void shouldConfirmPayment() {
        Policy policy = PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build();
        Boolean confirmed = policy.confirmPayment();

        Assertions.assertThat(confirmed).isTrue();
        Assertions.assertThat(policy.getPaymentPhase()).isEqualTo(PaymentPhase.CONFIRMED);
        Assertions.assertThat(policy.getPolicySolicitation()).isEqualTo(PolicySolicitation.PENDING);
    }

    @Test
    public void shouldRejectPayment() {
        Policy policy = PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build();
        Boolean confirmed = policy.rejectPayment();

        Assertions.assertThat(confirmed).isTrue();
        Assertions.assertThat(policy.getPaymentPhase()).isEqualTo(PaymentPhase.REJECTED);
        Assertions.assertThat(policy.getPolicySolicitation()).isEqualTo(PolicySolicitation.REJECTED);
    }

    @Test
    public void shouldConfirmSubscriptionAuthorization() {
        Policy policy = PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build();
        Boolean confirmed = policy.confirmSubscriptionAuthorization();

        Assertions.assertThat(confirmed).isTrue();
        Assertions.assertThat(policy.getSubscriptionAuthorizationPhase()).isEqualTo(SubscriptionAuthorizationPhase.CONFIRMED);
        Assertions.assertThat(policy.getPolicySolicitation()).isEqualTo(PolicySolicitation.PENDING);
    }

    @Test
    public void shouldRejectSubscriptionAuthorizationAndSolicitation() {
        Policy policy = PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build();
        Boolean confirmed = policy.rejectSubscriptionAuthorization();

        Assertions.assertThat(confirmed).isTrue();
        Assertions.assertThat(policy.getSubscriptionAuthorizationPhase()).isEqualTo(SubscriptionAuthorizationPhase.REJECTED);
        Assertions.assertThat(policy.getPolicySolicitation()).isEqualTo(PolicySolicitation.REJECTED);
    }

    @Test
    public void shouldApproveSolicitationWhenConfirmPaymentAndSubscriptionAuthorizationConfirmed() {
        Policy policy = PolicyProvider.create()
                .policySolicitation(PolicySolicitation.PENDING)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.CONFIRMED)
                .build();

        policy.confirmPayment();
        Assertions.assertThat(policy.getPolicySolicitation()).isEqualTo(PolicySolicitation.APPROVED);
    }

    @Test
    public void shouldApproveSolicitationWhenConfirmSubscriptionAuthorizationAndPaymentPhaseConfirmed() {
        Policy policy = PolicyProvider.create()
                .policySolicitation(PolicySolicitation.PENDING)
                .paymentPhase(PaymentPhase.CONFIRMED)
                .build();

        policy.confirmSubscriptionAuthorization();
        Assertions.assertThat(policy.getPolicySolicitation()).isEqualTo(PolicySolicitation.APPROVED);
    }

    @Test
    public void shouldDefinePolicyRequestWhenNoDefinitionExists() {
        Policy policy = PolicyProvider.create()
                .id(null)
                .policySolicitation(null)
                .paymentPhase(null)
                .subscriptionAuthorizationPhase(null)
                .build();

        Assertions.assertThat(policy.getPolicySolicitation()).isEqualTo(PolicySolicitation.RECEIVED);
        Assertions.assertThat(policy.getPaymentPhase()).isEqualTo(PaymentPhase.IN_PROGRESS);
        Assertions.assertThat(policy.getSubscriptionAuthorizationPhase()).isEqualTo(SubscriptionAuthorizationPhase.IN_PROGRESS);
    }

    @ParameterizedTest
    @MethodSource(value = "shouldFailUpdatePolicySolicitationSource")
    public void shouldFailUpdatePolicySolicitation(Policy policy, PolicySolicitation fromPolicySolicitation, PolicySolicitation toPolicySolicitation) {
        Assertions.assertThatThrownBy(() -> policy.updateSolicitation(toPolicySolicitation))
                .hasMessage("Policy solicitation cannot change from [%s] to status [%s]".formatted(fromPolicySolicitation, toPolicySolicitation))
                .isInstanceOf(EntityInvalidRuntimeException.class);
    }

    public static Stream<Arguments> shouldFailUpdatePolicySolicitationSource() {
        return Stream.of(
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.RECEIVED).build(), PolicySolicitation.RECEIVED, PolicySolicitation.RECEIVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.RECEIVED).build(), PolicySolicitation.RECEIVED, PolicySolicitation.APPROVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.VALIDATED).build(), PolicySolicitation.VALIDATED, PolicySolicitation.RECEIVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.VALIDATED).build(), PolicySolicitation.VALIDATED, PolicySolicitation.VALIDATED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build(), PolicySolicitation.PENDING, PolicySolicitation.PENDING),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build(), PolicySolicitation.PENDING, PolicySolicitation.VALIDATED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build(), PolicySolicitation.PENDING, PolicySolicitation.RECEIVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.REJECTED).build(), PolicySolicitation.REJECTED, PolicySolicitation.RECEIVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.REJECTED).build(), PolicySolicitation.REJECTED, PolicySolicitation.VALIDATED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.REJECTED).build(), PolicySolicitation.REJECTED, PolicySolicitation.REJECTED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.REJECTED).build(), PolicySolicitation.REJECTED, PolicySolicitation.APPROVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.REJECTED).build(), PolicySolicitation.REJECTED, PolicySolicitation.CANCELED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.APPROVED).build(), PolicySolicitation.APPROVED, PolicySolicitation.RECEIVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.APPROVED).build(), PolicySolicitation.APPROVED, PolicySolicitation.VALIDATED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.APPROVED).build(), PolicySolicitation.APPROVED, PolicySolicitation.PENDING),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.APPROVED).build(), PolicySolicitation.APPROVED, PolicySolicitation.REJECTED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.APPROVED).build(), PolicySolicitation.APPROVED, PolicySolicitation.APPROVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.APPROVED).build(), PolicySolicitation.APPROVED, PolicySolicitation.CANCELED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.CANCELED).build(), PolicySolicitation.CANCELED, PolicySolicitation.RECEIVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.CANCELED).build(), PolicySolicitation.CANCELED, PolicySolicitation.VALIDATED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.CANCELED).build(), PolicySolicitation.CANCELED, PolicySolicitation.PENDING),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.CANCELED).build(), PolicySolicitation.CANCELED, PolicySolicitation.REJECTED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.CANCELED).build(), PolicySolicitation.CANCELED, PolicySolicitation.APPROVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.CANCELED).build(), PolicySolicitation.CANCELED, PolicySolicitation.CANCELED)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "shouldBeAbleToUpdatePolicySolicitationSource")
    public void shouldBeAbleToUpdatePolicySolicitation(Policy policy, PolicySolicitation fromSolicitation, PolicySolicitation toSolicitation) {
        Assertions.assertThat(policy.updateSolicitation(toSolicitation).getPolicySolicitation()).isEqualTo(toSolicitation);
        Assertions.assertThat(policy.getHistories()).hasSize(1);
        Assertions.assertThat(policy.getHistories().get(0).getStatus()).isEqualTo(fromSolicitation);
    }

    public static Stream<Arguments> shouldBeAbleToUpdatePolicySolicitationSource() {
        return Stream.of(
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.RECEIVED).build(), PolicySolicitation.RECEIVED, PolicySolicitation.VALIDATED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.RECEIVED).build(), PolicySolicitation.RECEIVED, PolicySolicitation.PENDING),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.RECEIVED).build(), PolicySolicitation.RECEIVED, PolicySolicitation.CANCELED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.VALIDATED).build(), PolicySolicitation.VALIDATED, PolicySolicitation.PENDING),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.VALIDATED).build(), PolicySolicitation.VALIDATED, PolicySolicitation.APPROVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.VALIDATED).build(), PolicySolicitation.VALIDATED, PolicySolicitation.REJECTED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.VALIDATED).build(), PolicySolicitation.VALIDATED, PolicySolicitation.CANCELED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build(), PolicySolicitation.PENDING, PolicySolicitation.APPROVED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build(), PolicySolicitation.PENDING, PolicySolicitation.REJECTED),
                Arguments.of(PolicyProvider.create().policySolicitation(PolicySolicitation.PENDING).build(), PolicySolicitation.PENDING, PolicySolicitation.CANCELED)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "providerPolicyFailBuilder")
    public void shouldFailToCreatePolicyRequests(Policy.PolicyBuilder builder, String error) {
        Assertions.assertThatThrownBy(() -> builder.build())
                .isInstanceOf(EntityInvalidRuntimeException.class)
                .hasMessage(error);
    }

    public static Stream<Arguments> providerPolicyFailBuilder() {
        return Stream.of(
                Arguments.of(PolicyProvider.create().customerId(null), "Customer cannot be null."),
                Arguments.of(PolicyProvider.create().productId(null), "Product cannot be null."),
                Arguments.of(PolicyProvider.create().category(null), "Insurance category cannot be null."),
                Arguments.of(PolicyProvider.create().salesChannel(null), "Sales channel cannot be null."),
                Arguments.of(PolicyProvider.create().paymentMethod(null), "Payment method cannot be null."),
                Arguments.of(PolicyProvider.create().totalMonthlyPremiumAmount(null), "Total monthly premium amount cannot be null."),
                Arguments.of(PolicyProvider.create().insuredAmount(null), "Insured amount cannot be null."),
                Arguments.of(PolicyProvider.create().id(1L).policySolicitation(null), "Policy solicitation cannot be null."),
                Arguments.of(PolicyProvider.create().id(null).policySolicitation(PolicySolicitation.VALIDATED), "Policy request status cannot be other than [%s].".formatted(PolicySolicitation.RECEIVED)),
                Arguments.of(PolicyProvider.create().id(null).policySolicitation(PolicySolicitation.PENDING), "Policy request status cannot be other than [%s].".formatted(PolicySolicitation.RECEIVED)),
                Arguments.of(PolicyProvider.create().id(null).policySolicitation(PolicySolicitation.REJECTED), "Policy request status cannot be other than [%s].".formatted(PolicySolicitation.RECEIVED)),
                Arguments.of(PolicyProvider.create().id(null).policySolicitation(PolicySolicitation.APPROVED), "Policy request status cannot be other than [%s].".formatted(PolicySolicitation.RECEIVED)),
                Arguments.of(PolicyProvider.create().id(null).policySolicitation(PolicySolicitation.CANCELED), "Policy request status cannot be other than [%s].".formatted(PolicySolicitation.RECEIVED)),
                Arguments.of(PolicyProvider.create().id(null).paymentPhase(PaymentPhase.CONFIRMED), "It is not possible to create a new policy for this type of payment status [%s].".formatted(PaymentPhase.CONFIRMED)),
                Arguments.of(PolicyProvider.create().id(null).paymentPhase(PaymentPhase.REJECTED), "It is not possible to create a new policy for this type of payment status [%s].".formatted(PaymentPhase.REJECTED)),
                Arguments.of(PolicyProvider.create().id(null).subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.CONFIRMED), "It is not possible to create a new policy for this type of subscription authorization status [%s].".formatted(SubscriptionAuthorizationPhase.CONFIRMED)),
                Arguments.of(PolicyProvider.create().id(null).subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.REJECTED), "It is not possible to create a new policy for this type of subscription authorization status [%s].".formatted(SubscriptionAuthorizationPhase.REJECTED))
        );
    }
}
