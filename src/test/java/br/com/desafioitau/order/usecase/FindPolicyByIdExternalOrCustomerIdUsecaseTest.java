package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.controller.policy.response.GetPolicyResponse;
import br.com.desafioitau.order.exception.EntityNotFoundRuntimeException;
import br.com.desafioitau.order.filter.PolicyFilter;
import br.com.desafioitau.order.model.*;
import br.com.desafioitau.order.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.assertj.core.api.Assertions;
import org.mockito.MockitoAnnotations;
import support.PolicyProvider;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FindPolicyByIdExternalOrCustomerIdUsecaseTest {

    @Mock
    private PolicyRepository repository;
    @InjectMocks
    private FindPolicyByIdExternalOrCustomerIdUsecase usecase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPolicyResponseSuccessfully() {
        UUID idExternal = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        PolicyFilter filter = PolicyFilter.builder()
                .idExternal(idExternal)
                .customerId(customerId)
                .build();

        Assistance assistance = Assistance.builder()
                .assistance("GUINCHO")
                .build();

        PolicyHistory history = PolicyHistory.builder()
                .status(PolicySolicitation.RECEIVED)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        Policy policy = PolicyProvider.create()
                .idExternal(idExternal)
                .customerId(customerId)
                .productId(productId)
                .category(InsuranceCategory.LIFE)
                .salesChannel(SalesChannel.MOBILE)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .policySolicitation(PolicySolicitation.PENDING)
                .createdAt(LocalDateTime.now())
                .totalMonthlyPremiumAmount(new BigDecimal("89.90"))
                .insuredAmount(new BigDecimal("10000.00"))
                .assistances(List.of(assistance))
                .histories(List.of(history))
                .build();

        Mockito.when(repository.findByIdExternalOrCustomerId(filter)).thenReturn(Optional.of(policy));

        GetPolicyResponse response = usecase.execute(filter);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo(idExternal);
        Assertions.assertThat(response.getCustomerId()).isEqualTo(customerId);
        Assertions.assertThat(response.getProductId()).isEqualTo(productId);
        Assertions.assertThat(response.getCategory()).isEqualTo(InsuranceCategory.LIFE);
        Assertions.assertThat(response.getSalesChannel()).isEqualTo(SalesChannel.MOBILE);
        Assertions.assertThat(response.getPaymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
        Assertions.assertThat(response.getStatus()).isEqualTo(PolicySolicitation.PENDING);
        Assertions.assertThat(response.getAssistances()).containsExactly("GUINCHO");
        Assertions.assertThat(response.getHistory()).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenPolicyNotFound() {
        PolicyFilter filter = PolicyFilter.builder()
                .idExternal(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .build();

        Mockito.when(repository.findByIdExternalOrCustomerId(filter)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> usecase.execute(filter))
                .isInstanceOf(EntityNotFoundRuntimeException.class)
                .hasMessageContaining("Policy not found");
    }
}
