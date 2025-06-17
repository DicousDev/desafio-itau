package br.com.desafioitau.order.controller.policy.response;

import br.com.desafioitau.order.model.InsuranceCategory;
import br.com.desafioitau.order.model.PaymentMethod;
import br.com.desafioitau.order.model.PolicySolicitation;
import br.com.desafioitau.order.model.SalesChannel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class GetPolicyResponse {

    private UUID id;
    @JsonProperty("customer_id")
    private UUID customerId;
    @JsonProperty("product_id")
    private UUID productId;
    private InsuranceCategory category;
    private SalesChannel salesChannel;
    private PaymentMethod paymentMethod;
    private PolicySolicitation status;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    @JsonProperty("total_monthly_premium_amount")
    private BigDecimal totalMonthlyPremiumAmount;
    @JsonProperty("insured_amount")
    private BigDecimal insuredAmount;
    private List<String> assistances;
    private List<GetHistoryResponse> history;
}
