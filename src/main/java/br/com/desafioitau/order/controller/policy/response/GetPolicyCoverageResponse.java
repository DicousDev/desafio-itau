package br.com.desafioitau.order.controller.policy.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class GetPolicyCoverageResponse {

    private String title;
    private BigDecimal amount;
}
