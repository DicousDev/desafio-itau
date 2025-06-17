package br.com.desafioitau.order.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ValidPolicyRequest {

    private UUID policyId;
    private UUID customerId;
}
