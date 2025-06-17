package br.com.desafioitau.order.schedule;

import br.com.desafioitau.order.usecase.EvaluatePolicyRequestUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvaluatePolicyRequestSchedule {

    private final EvaluatePolicyRequestUsecase evaluatePolicyRequestUsecase;

    @Scheduled(fixedRate = 10_000)
    public void execute() {
        log.info("Starting Evaluate Policy Request Job execution!");
        evaluatePolicyRequestUsecase.execute();
        log.info("Finished Evaluate Policy Request Job!");
    }
}
