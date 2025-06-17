package support;

import br.com.desafioitau.order.model.InsuranceCategory;
import br.com.desafioitau.order.model.PaymentMethod;
import br.com.desafioitau.order.model.PaymentPhase;
import br.com.desafioitau.order.model.Policy;
import br.com.desafioitau.order.model.PolicySolicitation;
import br.com.desafioitau.order.model.SalesChannel;
import br.com.desafioitau.order.model.SubscriptionAuthorizationPhase;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PolicyProvider {

    public static Policy.PolicyBuilder create() {
        return Policy.builder()
                .id(1L)
                .idExternal(UUID4T.UUID_0)
                .customerId(UUID4T.UUID_0)
                .productId(UUID4T.UUID_0)
                .category(InsuranceCategory.AUTO)
                .salesChannel(SalesChannel.WHATSAPP)
                .paymentMethod(PaymentMethod.PIX)
                .paymentPhase(PaymentPhase.IN_PROGRESS)
                .subscriptionAuthorizationPhase(SubscriptionAuthorizationPhase.IN_PROGRESS)
                .totalMonthlyPremiumAmount(BigDecimal.valueOf(80000))
                .insuredAmount(BigDecimal.valueOf(75000))
                .assistances(new ArrayList<>())
                .policySolicitation(PolicySolicitation.RECEIVED)
                .histories(new ArrayList<>());
    }
}
