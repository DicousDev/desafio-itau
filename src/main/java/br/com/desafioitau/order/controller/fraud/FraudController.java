package br.com.desafioitau.order.controller.fraud;

import br.com.desafioitau.order.dto.CustomerRatingDTO;
import br.com.desafioitau.order.model.CustomerType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/fraud")
public class FraudController {

    @PostMapping("/valid")
    public CustomerRatingDTO validPolicy() {
        return CustomerRatingDTO.builder()
                .policyId(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .analyzedAt(LocalDateTime.now())
                .classification(CustomerType.HIGH_RISK)
                .occurrences(Collections.emptyList())
                .build();
    }
}
