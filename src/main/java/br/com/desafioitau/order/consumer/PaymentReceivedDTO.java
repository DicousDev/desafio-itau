package br.com.desafioitau.order.consumer;

import br.com.desafioitau.order.model.PaymentPhase;
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
public class PaymentReceivedDTO {

    private UUID policyIdExternal;
    private PaymentPhase paymentPhase;
}
