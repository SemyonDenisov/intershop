package ru.yandex.payment.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonTypeName;


import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * CartPaymentRequest
 */

@JsonTypeName("cartPayment_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-19T20:36:16.873307+04:00[Europe/Samara]", comments = "Generator version: 7.5.0")
public class CartPaymentRequest {

    private Integer userId;

    private Double amount;

    /**
     * Get userId
     *
     * @return userId
     */
    @NotNull
    @Schema(name = "userId", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }

    /**
     * Get amount
     *
     * @return amount
     */
    @NotNull
    @Schema(name = "amount", example = "500.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    public CartPaymentRequest(Integer userId,Double amount) {
        this.userId = userId;
        this.amount = amount;
    }
}

