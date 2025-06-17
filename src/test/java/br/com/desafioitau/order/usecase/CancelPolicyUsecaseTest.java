package br.com.desafioitau.order.usecase;

import br.com.desafioitau.order.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import br.com.desafioitau.order.exception.EntityNotFoundRuntimeException;
import br.com.desafioitau.order.model.Policy;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.assertj.core.api.Assertions;
import support.UUID4T;

import java.util.Optional;

public class CancelPolicyUsecaseTest {

    @Mock
    private PolicyRepository repository;
    @InjectMocks
    private CancelPolicyUsecase cancelPolicyUsecase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCancelPolicySuccessfully() {
        Policy policy = Mockito.mock(Policy.class);
        Policy cancelledPolicy = Mockito.mock(Policy.class);

        Mockito.when(repository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.of(policy));
        Mockito.when(policy.cancel()).thenReturn(cancelledPolicy);

        cancelPolicyUsecase.execute(UUID4T.UUID_0);

        ArgumentCaptor<Policy> captor = ArgumentCaptor.forClass(Policy.class);
        Mockito.verify(repository).save(captor.capture());
        Mockito.verify(policy).cancel();

        Assertions.assertThat(captor.getValue()).isEqualTo(cancelledPolicy);
    }

    @Test
    void shouldThrowExceptionWhenPolicyNotFound() {
        Mockito.when(repository.findByIdExternal(UUID4T.UUID_0)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> cancelPolicyUsecase.execute(UUID4T.UUID_0))
                .isInstanceOf(EntityNotFoundRuntimeException.class)
                .hasMessageContaining("Policy not found");
    }
}
