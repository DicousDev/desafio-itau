package br.com.desafioitau.order.controller.policy.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PolicyCoverageRequest {

    private String title;
    private BigDecimal amount;
}
