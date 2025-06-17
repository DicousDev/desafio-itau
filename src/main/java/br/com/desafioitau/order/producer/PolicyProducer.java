package br.com.desafioitau.order.producer;

import br.com.desafioitau.order.model.Policy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PolicyProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final ObjectMapper mapper;

    public PolicyProducer(RabbitTemplate rabbitTemplate, @Value("POLICY_EXCHANGE_NAME") String exchangeName, ObjectMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.mapper = mapper;
    }

    public void sendPolicy(Policy policy) {
        try {
            String json = mapper.writeValueAsString(policy);
            rabbitTemplate.convertAndSend(exchangeName, "policy", json);
        } catch (JsonProcessingException e) {
            log.error("Error trying to convert object to JSON");
        }

    }
}
