package br.com.desafioitau.order.feign;

import br.com.desafioitau.order.dto.CustomerRatingDTO;
import br.com.desafioitau.order.dto.ValidPolicyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "FraudApiClient", name = "fraud", url = "${FRAUD_API}")
public interface FraudFeignClient {

    @PostMapping(value = "/valid", consumes = "application/json", produces = "application/json")
    CustomerRatingDTO validPolicy(@RequestBody ValidPolicyRequest request);
}
