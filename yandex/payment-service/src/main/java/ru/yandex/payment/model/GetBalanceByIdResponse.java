package ru.yandex.payment.model;


import com.fasterxml.jackson.annotation.JsonTypeName;


import jakarta.annotation.Generated;

/**
 * GetBalanceById200Response
 */

@JsonTypeName("getBalanceById_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-19T20:36:16.873307+04:00[Europe/Samara]", comments = "Generator version: 7.5.0")
public class GetBalanceByIdResponse {

    private Integer userId;

    private Double balance;

    public GetBalanceByIdResponse(Integer userId, Double balance) {
        this.balance = balance;
        this.userId = userId;
    }
}

