package integration;

import br.com.desafioitau.order.model.PaymentPhase;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.model.SubscriptionAuthorizationPhase;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.jdbc.Sql;
import support.ITemplate;
import support.UUID4T;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SubscriptionAuthorizationConsumerTest extends ITemplate {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("SUBSCRIPTION_AUTHORIZATION_QUEUE")
    private String subscriptionAuthorizationQueue;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-policies.sql"})
    public void shouldConsumerMessages() throws IOException, TimeoutException, InterruptedException {
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);

        channel.exchangeDeclare("SUBSCRIPTION_AUTHORIZATION_EXCHANGE", BuiltinExchangeType.DIRECT);
        channel.queueBind(subscriptionAuthorizationQueue, "SUBSCRIPTION_AUTHORIZATION_EXCHANGE", "SUBSCRIPTION");

        String subscriptionAuthorizationRequestJson = readJSON("IT/subscription-authorization-queue-request.json");
        rabbitTemplate.convertAndSend("SUBSCRIPTION_AUTHORIZATION_EXCHANGE", "SUBSCRIPTION", subscriptionAuthorizationRequestJson);

        CountDownLatch latch = new CountDownLatch(1);
        latch.await(2, TimeUnit.SECONDS);
        Policy policy = entityManager.createQuery("SELECT p FROM Policy p WHERE idExternal=:id", Policy.class)
                .setParameter("id", UUID4T.UUID_0)
                .getSingleResult();

        Assertions.assertThat(policy.getSubscriptionAuthorizationPhase()).isEqualTo(SubscriptionAuthorizationPhase.CONFIRMED);

        channel.close();
        connection.close();
    }
}
