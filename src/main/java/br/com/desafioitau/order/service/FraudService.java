package br.com.desafioitau.order.service;

import br.com.desafioitau.order.dto.CustomerRatingDTO;
import br.com.desafioitau.order.dto.ValidPolicyRequest;
import br.com.desafioitau.order.feign.FraudFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudService {

    private final FraudFeignClient fraudFeignClient;

    public CustomerRatingDTO validPolicy(ValidPolicyRequest request) {
        return fraudFeignClient.validPolicy(request);
    }
}
