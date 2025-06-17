package br.com.desafioitau.order.settings;

import br.com.desafioitau.order.repository.PolicyRepository;
import br.com.desafioitau.order.repository.postgres.PolicyRepositoryImp;
import br.com.desafioitau.order.repository.postgres.jpa.PolicyRepositoryJpa;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class BeanSetting {

    @Bean
    public PolicyRepository beanPolicyRepository(PolicyRepositoryJpa jpa) {
        return new PolicyRepositoryImp(jpa);
    }

    @Bean
    public Queue subscriptionAuthorizationQueue(@Value("${SUBSCRIPTION_AUTHORIZATION_QUEUE}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue paymentQueue(@Value("${PAYMENT_QUEUE}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue policyQueue(@Value("${POLICY_QUEUE}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange directExchange(@Value("POLICY_EXCHANGE_NAME") String exchange) {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding(Queue policyQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(policyQueue).to(directExchange).with("policy");
    }
}
