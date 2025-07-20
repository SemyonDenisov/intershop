package ru.yandex.payment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.payment.model.CartPaymentResponse;
import ru.yandex.payment.model.CartPaymentRequest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import jakarta.annotation.Generated;
import ru.yandex.payment.service.BalanceService;

import static ru.yandex.payment.model.Action.MINUS;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-19T20:36:16.873307+04:00[Europe/Samara]", comments = "Generator version: 7.5.0")
@RestController
public class PaymentApiController implements PaymentApi {

    private final BalanceService balanceService;

    public PaymentApiController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }


    /**
     * POST /payment : Оплата заказа
     *
     * @param cartPaymentRequest (required)
     * @return Платёж успешно выполнен (status code 200)
     * or Недостаточно средств (status code 400)
     * or Пользователь не найден (status code 404)
     */
    @Operation(
            operationId = "cartPayment",
            summary = "Оплата заказа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Платёж успешно выполнен", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CartPaymentResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Недостаточно средств", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CartPaymentResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/payment",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public Mono<ResponseEntity<CartPaymentResponse>> cartPayment(
            @Parameter(name = "CartPaymentRequest", description = "", required = true) @Valid @RequestBody Mono<CartPaymentRequest> cartPaymentRequest,
            @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        return cartPaymentRequest
                .flatMap(cr -> balanceService.changeBalance(cr.getUserId(), cr.getAmount(), MINUS))
                .map(ResponseEntity::ok)
                .onErrorResume(th -> Mono.just(ResponseEntity.badRequest().body(new CartPaymentResponse("FAILED", -1.0))));
    }
}
