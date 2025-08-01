/*
 * Payment Service API
 * API для проверки баланса и оформления заказа
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ru.yandex.payment.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * CartPaymentRequest
 */
@JsonPropertyOrder({
  CartPaymentRequest.JSON_PROPERTY_USER_ID,
  CartPaymentRequest.JSON_PROPERTY_AMOUNT
})
@JsonTypeName("cartPayment_request")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-07-18T22:36:48.959781700+04:00[Europe/Samara]", comments = "Generator version: 7.12.0")
public class CartPaymentRequest {
  public static final String JSON_PROPERTY_USER_ID = "userId";
  @jakarta.annotation.Nonnull
  private Integer userId;

  public static final String JSON_PROPERTY_AMOUNT = "amount";
  @jakarta.annotation.Nonnull
  private Double amount;

  public CartPaymentRequest() {
  }

  public CartPaymentRequest(Integer userId, Double amount) {
    this.userId = userId;
    this.amount = amount;
  }

  public CartPaymentRequest userId(@jakarta.annotation.Nonnull Integer userId) {
    
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
   */
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_USER_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getUserId() {
    return userId;
  }


  @JsonProperty(JSON_PROPERTY_USER_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setUserId(@jakarta.annotation.Nonnull Integer userId) {
    this.userId = userId;
  }

  public CartPaymentRequest amount(@jakarta.annotation.Nonnull Double amount) {
    
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AMOUNT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Double getAmount() {
    return amount;
  }


  @JsonProperty(JSON_PROPERTY_AMOUNT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAmount(@jakarta.annotation.Nonnull Double amount) {
    this.amount = amount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CartPaymentRequest cartPaymentRequest = (CartPaymentRequest) o;
    return Objects.equals(this.userId, cartPaymentRequest.userId) &&
        Objects.equals(this.amount, cartPaymentRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CartPaymentRequest {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

