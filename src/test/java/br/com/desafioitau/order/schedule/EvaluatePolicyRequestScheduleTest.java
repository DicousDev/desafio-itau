package br.com.desafioitau.order.schedule;

import br.com.desafioitau.order.usecase.EvaluatePolicyRequestUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class EvaluatePolicyRequestScheduleTest {

    @InjectMocks
    private EvaluatePolicyRequestSchedule evaluatePolicyRequestSchedule;
    @Mock
    private EvaluatePolicyRequestUsecase evaluatePolicyRequestUsecase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldExecuteJob() {
        evaluatePolicyRequestSchedule.execute();
        Mockito.verify(evaluatePolicyRequestUsecase).execute();
    }
}
