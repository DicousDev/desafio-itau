package br.com.desafioitau.order.consumer;

import br.com.desafioitau.order.usecase.UpdatePaymentPhaseUsecase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final ObjectMapper mapper;
    private final UpdatePaymentPhaseUsecase updatePaymentPhaseUsecase;

    @RabbitListener(queues = "${PAYMENT_QUEUE}")
    public void receiveMessage(String body) {
        PaymentReceivedDTO paymentReceivedDTO;
        try {
            paymentReceivedDTO = mapper.readValue(body, PaymentReceivedDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error reading invalid JSON");
            return;
        }

        if(Objects.isNull(paymentReceivedDTO.getPolicyIdExternal()) || Objects.isNull(paymentReceivedDTO.getPaymentPhase())) {
            log.error("Policy ID or payment phase cannot be null.");
            return;
        }

        updatePaymentPhaseUsecase.execute(paymentReceivedDTO);
    }
}
