package br.com.desafioitau.order.dto;

import br.com.desafioitau.order.model.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRatingDTO {

    private UUID policyId;
    private UUID customerId;
    private LocalDateTime analyzedAt;
    private CustomerType classification;
    private List<OccurrenceDTO> occurrences;
}
