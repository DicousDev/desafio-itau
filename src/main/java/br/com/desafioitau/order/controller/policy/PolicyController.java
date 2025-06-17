package br.com.desafioitau.order.controller.policy;

import br.com.desafioitau.order.controller.policy.request.CreatePolicyRequest;
import br.com.desafioitau.order.controller.policy.response.CreatePolicyResponse;
import br.com.desafioitau.order.controller.policy.response.GetPolicyResponse;
import br.com.desafioitau.order.controlleradvice.ApiResponseError;
import br.com.desafioitau.order.exception.EntityInvalidRuntimeException;
import br.com.desafioitau.order.filter.PolicyFilter;
import br.com.desafioitau.order.usecase.CancelPolicyUsecase;
import br.com.desafioitau.order.usecase.CreatePolicyUsecase;
import br.com.desafioitau.order.usecase.FindPolicyByIdExternalOrCustomerIdUsecase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final CreatePolicyUsecase createPolicyUsecase;
    private final FindPolicyByIdExternalOrCustomerIdUsecase findPolicyByIdExternalOrCustomerIdUsecase;
    private final CancelPolicyUsecase cancelPolicyUsecase;

    @Operation(summary = "Find Policy by ID external or customer ID.",
            responses = {
                    @ApiResponse(
                            description = "Policy found successfully.",
                            responseCode = "200",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Void.class),
                                            mediaType = MediaType.APPLICATION_JSON_VALUE
                                    )
                            }
                    ),
                    @ApiResponse(
                            description = "Policy not found.",
                            responseCode = "404",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ApiResponseError.class),
                                            mediaType = MediaType.APPLICATION_JSON_VALUE
                                    )
                            }
                    ),
                    @ApiResponse(
                            description = "Unexpected server error.",
                            responseCode = "500",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ApiResponseError.class),
                                            mediaType = MediaType.APPLICATION_JSON_VALUE
                                    )
                            }
                    )
            }
    )
    @GetMapping
    public GetPolicyResponse execute(@RequestParam(value = "idExternal", required = false) UUID idExternal,
                                     @RequestParam(value = "customerId", required = false) UUID customerId) {

        if(Objects.isNull(idExternal) && Objects.isNull(customerId)) {
            throw new EntityInvalidRuntimeException("One parameter is required");
        }

        PolicyFilter filter = PolicyFilter.builder()
                .idExternal(idExternal)
                .customerId(customerId)
                .build();

        return findPolicyByIdExternalOrCustomerIdUsecase.execute(filter);
    }

    @Operation(summary = "Create a new policy.",
            responses = {
                    @ApiResponse(
                            description = "Policy added successfully.",
                            responseCode = "201",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Void.class),
                                            mediaType = MediaType.APPLICATION_JSON_VALUE
                                    )
                            }
                    ),
                    @ApiResponse(
                            description = "Payload invalid.",
                            responseCode = "400",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ApiResponseError.class),
                                            mediaType = MediaType.APPLICATION_JSON_VALUE
                                    )
                            }
                    ),
                    @ApiResponse(
                            description = "Unexpected server error.",
                            responseCode = "500",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ApiResponseError.class),
                                            mediaType = MediaType.APPLICATION_JSON_VALUE
                                    )
                            }
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePolicyResponse createPolicy(@RequestBody @Valid CreatePolicyRequest request) {
        return createPolicyUsecase.execute(request);
    }

    @Operation(summary = "Cancel policy",
            responses = {
                    @ApiResponse(
                            description = "Policy canceled successfully.",
                            responseCode = "201",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Void.class),
                                            mediaType = MediaType.APPLICATION_JSON_VALUE
                                    )
                            }
                    ),
                    @ApiResponse(
                            description = "Policy not found.",
                            responseCode = "404",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ApiResponseError.class),
                                            mediaType = MediaType.APPLICATION_JSON_VALUE
                                    )
                            }
                    ),
                    @ApiResponse(
                            description = "Unexpected server error.",
                            responseCode = "500",
                            useReturnTypeSchema = true,
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ApiResponseError.class),
                                            mediaType = MediaType.APPLICATION_JSON_VALUE
                                    )
                            }
                    )
            }
    )
    @PatchMapping("/{id}")
    public void cancelPolicy(@PathVariable("id") UUID idExternal) {
        cancelPolicyUsecase.execute(idExternal);
    }
}
