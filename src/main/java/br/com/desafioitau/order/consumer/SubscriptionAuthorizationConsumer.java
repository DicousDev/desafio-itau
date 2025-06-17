package br.com.desafioitau.order.consumer;

import br.com.desafioitau.order.usecase.UpdateSubscriptionAuthorizationPhaseUsecase;
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
public class SubscriptionAuthorizationConsumer {

    private final ObjectMapper mapper;
    private final UpdateSubscriptionAuthorizationPhaseUsecase updateSubscriptionAuthorizationPhaseUsecase;

    @RabbitListener(queues = "${SUBSCRIPTION_AUTHORIZATION_QUEUE}")
    public void receiveMessage(String body) {
        SubscriptionAuthorizationReceivedDTO subscriptionAuthorizationReceived;
        try {
            subscriptionAuthorizationReceived = mapper.readValue(body, SubscriptionAuthorizationReceivedDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error reading invalid JSON");
            return;
        }

        if(Objects.isNull(subscriptionAuthorizationReceived.getPolicyIdExternal()) || Objects.isNull(subscriptionAuthorizationReceived.getSubscriptionAuthorizationPhase())) {
            log.error("Policy ID or subscription authorization phase cannot be null.");
            return;
        }

        updateSubscriptionAuthorizationPhaseUsecase.execute(subscriptionAuthorizationReceived);
    }
}
