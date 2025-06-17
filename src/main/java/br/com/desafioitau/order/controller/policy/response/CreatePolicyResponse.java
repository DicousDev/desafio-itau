package br.com.desafioitau.order.controller.policy.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class CreatePolicyResponse {

    private UUID policyId;
    private LocalDateTime timestamp;
}
