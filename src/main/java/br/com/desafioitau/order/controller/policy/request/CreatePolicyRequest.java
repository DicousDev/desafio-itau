package br.com.desafioitau.order.controller.policy.request;

import br.com.desafioitau.order.model.InsuranceCategory;
import br.com.desafioitau.order.model.PaymentMethod;
import br.com.desafioitau.order.model.SalesChannel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class CreatePolicyRequest {

    @JsonProperty("customer_id")
    private UUID customerId;
    @JsonProperty("product_id")
    private UUID productId;
    private InsuranceCategory category;
    private SalesChannel salesChannel;
    private PaymentMethod paymentMethod;
    @JsonProperty("total_monthly_premium_amount")
    private BigDecimal totalMonthlyPremiumAmount;
    @JsonProperty("insured_amount")
    private BigDecimal insuredAmount;

    @NotNull
    private List<PolicyCoverageRequest> coverages;

    @NotNull
    private List<String> assistances;
}
