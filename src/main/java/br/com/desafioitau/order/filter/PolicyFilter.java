package br.com.desafioitau.order.filter;

import br.com.desafioitau.order.model.PolicySolicitation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PolicyFilter {

    private UUID idExternal;
    private UUID customerId;
    private Integer page;
    private Integer size;
    private List<PolicySolicitation> policySolicitation;
}
