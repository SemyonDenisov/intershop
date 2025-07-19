package ru.yandex.payment.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * CartPayment200Response
 */

@JsonTypeName("cartPayment_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-19T20:36:16.873307+04:00[Europe/Samara]", comments = "Generator version: 7.5.0")
public class CartPaymentResponse {

  private String status;

  private Double newBalance;

  public CartPaymentResponse(String status, Double newBalance) {
    this.status = status;
    this.newBalance = newBalance;
  }

  public CartPaymentResponse status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  
  @Schema(name = "status", example = "SUCCESS", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public CartPaymentResponse newBalance(Double newBalance) {
    this.newBalance = newBalance;
    return this;
  }

  /**
   * Get newBalance
   * @return newBalance
  */
  
  @Schema(name = "newBalance", example = "1000.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("newBalance")
  public Double getNewBalance() {
    return newBalance;
  }

  public void setNewBalance(Double newBalance) {
    this.newBalance = newBalance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CartPaymentResponse cartPayment200Response = (CartPaymentResponse) o;
    return Objects.equals(this.status, cartPayment200Response.status) &&
        Objects.equals(this.newBalance, cartPayment200Response.newBalance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, newBalance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CartPayment200Response {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    newBalance: ").append(toIndentedString(newBalance)).append("\n");
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

