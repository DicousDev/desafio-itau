package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.consumer.SubscriptionAuthorizationReceivedDTO;
import br.com.desafioitau.order.model.PaymentPhase;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.model.PolicySolicitation;
import br.com.desafioitau.order.model.SubscriptionAuthorizationPhase;
import br.com.desafioitau.order.producer.PolicyProducer;
import br.com.desafioitau.order.repository.PolicyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import support.PolicyProvider;
import support.UUID4T;

import java.math.BigDecimal;
import java.util.Optional;

public class UpdateSubscriptionAuthorizationPhaseUsecaseTest {

    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private PolicyProducer policyProducer;
    @InjectMocks
    private UpdateSubscriptionAuthorizationPhaseUsecase usecase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdatePhaseToConfirmedANDReturnPolicyUpdated() {
        Policy policy = PolicyProvider.create()
                .totalMonthlyPremiumAmount(BigDecimal.TEN)
                .insuredAmount(BigDecimal.TEN)
                .paymentPhase(PaymentPhase.CONFIRMED)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.IN_PROGRESS)
                .policySolicitation(PolicySolicitation.PENDING)
                .build();

        Mockito.when(policyRepository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.of(policy));
        Mockito.when(policyRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        SubscriptionAuthorizationReceivedDTO dto = SubscriptionAuthorizationReceivedDTO.builder()
                .policyIdExternal(UUID4T.UUID_0)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.CONFIRMED)
                .build();

        Policy result = usecase.execute(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSubscriptionAuthorizationPhase())
                .isEqualTo(SubscriptionAuthorizationPhase.CONFIRMED);

        Mockito.verify(policyProducer).sendPolicy(result);
        Mockito.verify(policyRepository).save(result);
    }

    @Test
    void shouldUpdatePhaseToRejectedAndReturnPolicyUpdated() {

        Policy policy = PolicyProvider.create()
                .totalMonthlyPremiumAmount(BigDecimal.TEN)
                .insuredAmount(BigDecimal.TEN)
                .paymentPhase(PaymentPhase.IN_PROGRESS)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.IN_PROGRESS)
                .policySolicitation(PolicySolicitation.RECEIVED)
                .build();

        Mockito.when(policyRepository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.of(policy));
        Mockito.when(policyRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        SubscriptionAuthorizationReceivedDTO dto = SubscriptionAuthorizationReceivedDTO.builder()
                .policyIdExternal(UUID4T.UUID_0)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.REJECTED)
                .build();

        Policy result = usecase.execute(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSubscriptionAuthorizationPhase())
                .isEqualTo(SubscriptionAuthorizationPhase.REJECTED);

        Mockito.verify(policyProducer).sendPolicy(result);
        Mockito.verify(policyRepository).save(result);
    }

    @Test
    void shouldReturnNullWhenPolicyNotFound() {

        Mockito.when(policyRepository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.empty());

        SubscriptionAuthorizationReceivedDTO dto = SubscriptionAuthorizationReceivedDTO.builder()
                .policyIdExternal(UUID4T.UUID_0)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.CONFIRMED)
                .build();

        Policy result = usecase.execute(dto);

        Assertions.assertThat(result).isNull();
        Mockito.verify(policyProducer, Mockito.never()).sendPolicy(Mockito.any());
        Mockito.verify(policyRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldNotUpdatePhaseIfNotInProgress() {

        Policy policy = PolicyProvider.create()
                .totalMonthlyPremiumAmount(BigDecimal.TEN)
                .insuredAmount(BigDecimal.TEN)
                .paymentPhase(PaymentPhase.IN_PROGRESS)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.CONFIRMED)
                .policySolicitation(PolicySolicitation.RECEIVED)
                .build();

        Mockito.when(policyRepository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.of(policy));
        Mockito.when(policyRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        SubscriptionAuthorizationReceivedDTO dto = SubscriptionAuthorizationReceivedDTO.builder()
                .policyIdExternal(UUID4T.UUID_0)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.CONFIRMED)
                .build();

        Policy result = usecase.execute(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSubscriptionAuthorizationPhase())
                .isEqualTo(SubscriptionAuthorizationPhase.CONFIRMED); // já estava confirmado

        Mockito.verify(policyProducer, Mockito.never()).sendPolicy(Mockito.any());
        Mockito.verify(policyRepository).save(result);
    }
}
