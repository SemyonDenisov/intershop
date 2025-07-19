package ru.yandex.payment.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CartPayment200Response
 */

@JsonTypeName("cartPayment_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-07-18T22:28:07.231021500+04:00[Europe/Samara]", comments = "Generator version: 7.5.0")
public class CartPayment200Response {

  private String status;

  private Double newBalance;

  public CartPayment200Response status(String status) {
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

  public CartPayment200Response newBalance(Double newBalance) {
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
    CartPayment200Response cartPayment200Response = (CartPayment200Response) o;
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

