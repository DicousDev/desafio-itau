package br.com.desafioitau.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OccurrenceDTO {

    private UUID id;
    private Long productId;
    private String type;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
