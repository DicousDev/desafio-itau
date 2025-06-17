package br.com.desafioitau.order.controller.policy.response;

import br.com.desafioitau.order.model.PolicySolicitation;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class GetHistoryResponse {

    private PolicySolicitation status;
    private LocalDateTime timestamp;
}
