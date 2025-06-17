package br.com.desafioitau.order.consumer;

import br.com.desafioitau.order.model.SubscriptionAuthorizationPhase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionAuthorizationReceivedDTO {

    private UUID policyIdExternal;
    private SubscriptionAuthorizationPhase subscriptionAuthorizationPhase;
}
