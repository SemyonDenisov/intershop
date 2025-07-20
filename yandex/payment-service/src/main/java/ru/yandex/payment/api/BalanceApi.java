package ru.yandex.payment.api;

import ru.yandex.payment.model.GetBalanceByIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-19T20:36:16.873307+04:00[Europe/Samara]", comments = "Generator version: 7.5.0")
@Validated
@Tag(name = "balance", description = "the balance API")
public interface BalanceApi {

    /**
     * GET /balance/{userId} : Получение баланса
     *
     * @param userId Идентификатор пользователя (required)
     * @return Успешный ответ (status code 200)
     * or Пользователь не найден (status code 404)
     */
    @Operation(
            operationId = "getBalanceById",
            summary = "Получение баланса",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный ответ", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GetBalanceByIdResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/balance/{userId}",
            produces = {"application/json"}
    )
    Mono<ResponseEntity<GetBalanceByIdResponse>> getBalanceById(
            @Parameter(name = "userId", description = "Идентификатор пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Integer userId,
            @Parameter(hidden = true) final ServerWebExchange exchange
    );

}
