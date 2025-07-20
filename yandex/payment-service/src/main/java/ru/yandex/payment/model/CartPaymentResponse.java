package ru.yandex.payment.model;


import com.fasterxml.jackson.annotation.JsonTypeName;


import jakarta.annotation.Generated;
import lombok.Getter;

/**
 * CartPayment200Response
 */

@JsonTypeName("cartPayment_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-19T20:36:16.873307+04:00[Europe/Samara]", comments = "Generator version: 7.5.0")
@Getter
public class CartPaymentResponse {

    private String status;

    private Double newBalance;

    public CartPaymentResponse(String status, Double newBalance) {
        this.status = status;
        this.newBalance = newBalance;
    }
}

