package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.consumer.PaymentReceivedDTO;
import br.com.desafioitau.order.model.PaymentPhase;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.producer.PolicyProducer;
import br.com.desafioitau.order.repository.PolicyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import support.UUID4T;

import java.util.Optional;

public class UpdatePaymentPhaseUsecaseTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private PolicyProducer policyProducer;

    @InjectMocks
    private UpdatePaymentPhaseUsecase updatePaymentPhaseUsecase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnNullWhenPolicyNotFound() {
        PaymentReceivedDTO dto = PaymentReceivedDTO.builder()
                .policyIdExternal(UUID4T.UUID_0)
                .paymentPhase(PaymentPhase.CONFIRMED)
                .build();

        Mockito.when(policyRepository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.empty());

        Policy result = updatePaymentPhaseUsecase.execute(dto);

        Assertions.assertThat(result).isNull();
        Mockito.verify(policyRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(policyProducer, Mockito.never()).sendPolicy(Mockito.any());
    }

    @Test
    void shouldConfirmPaymentSuccessfully() {
        PaymentReceivedDTO dto = PaymentReceivedDTO.builder()
                .policyIdExternal(UUID4T.UUID_0)
                .paymentPhase(PaymentPhase.CONFIRMED)
                .build();

        Policy policy = Mockito.mock(Policy.class);

        Mockito.when(policyRepository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.of(policy));
        Mockito.when(policy.confirmPayment()).thenReturn(true);
        Mockito.when(policyRepository.save(policy)).thenReturn(policy);

        Policy result = updatePaymentPhaseUsecase.execute(dto);

        Assertions.assertThat(result).isEqualTo(policy);
        Mockito.verify(policy).confirmPayment();
        Mockito.verify(policyProducer).sendPolicy(policy);
        Mockito.verify(policyRepository).save(policy);
    }

    @Test
    void shouldRejectPaymentSuccessfully() {
        PaymentReceivedDTO dto = PaymentReceivedDTO.builder()
                .policyIdExternal(UUID4T.UUID_0)
                .paymentPhase(PaymentPhase.REJECTED)
                .build();

        Policy policy = Mockito.mock(Policy.class);

        Mockito.when(policyRepository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.of(policy));
        Mockito.when(policy.rejectPayment()).thenReturn(true);
        Mockito.when(policyRepository.save(policy)).thenReturn(policy);

        Policy result = updatePaymentPhaseUsecase.execute(dto);

        Assertions.assertThat(result).isEqualTo(policy);
        Mockito.verify(policy).rejectPayment();
        Mockito.verify(policyProducer).sendPolicy(policy);
        Mockito.verify(policyRepository).save(policy);
    }

    @Test
    void shouldNotUpdateIfConfirmationFails() {
        PaymentReceivedDTO dto = PaymentReceivedDTO.builder()
                .policyIdExternal(UUID4T.UUID_0)
                .paymentPhase(PaymentPhase.CONFIRMED)
                .build();

        Policy policy = Mockito.mock(Policy.class);

        Mockito.when(policyRepository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.of(policy));
        Mockito.when(policy.confirmPayment()).thenReturn(false);
        Mockito.when(policyRepository.save(policy)).thenReturn(policy);

        Policy result = updatePaymentPhaseUsecase.execute(dto);

        Assertions.assertThat(result).isEqualTo(policy);
        Mockito.verify(policy).confirmPayment();
        Mockito.verify(policyProducer, Mockito.never()).sendPolicy(Mockito.any());
        Mockito.verify(policyRepository).save(policy);
    }
}
