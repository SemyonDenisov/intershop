package ru.yandex.payment.client.api;

import org.springframework.web.bind.annotation.RestController;
import ru.yandex.payment.client.ApiClient;

import ru.yandex.payment.client.model.CartPayment200Response;
import ru.yandex.payment.client.model.CartPayment400Response;
import ru.yandex.payment.client.model.CartPaymentRequest;
import ru.yandex.payment.client.model.GetBalanceById200Response;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-07-18T22:36:48.959781700+04:00[Europe/Samara]", comments = "Generator version: 7.12.0")
@RestController
public class DefaultApi {
    private ApiClient apiClient;

    public DefaultApi(String paymentServiceName, int paymentServicePort) {
        this(new ApiClient(paymentServiceName, paymentServicePort));
    }

    @Autowired
    public DefaultApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }


    /**
     * Оплата заказа
     *
     * <p><b>200</b> - Платёж успешно выполнен
     * <p><b>400</b> - Недостаточно средств
     * <p><b>404</b> - Пользователь не найден
     *
     * @param cartPaymentRequest The cartPaymentRequest parameter
     * @return CartPayment200Response
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec cartPaymentRequestCreation(CartPaymentRequest cartPaymentRequest) throws WebClientResponseException {
        Object postBody = cartPaymentRequest;
        // verify the required parameter 'cartPaymentRequest' is set
        if (cartPaymentRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'cartPaymentRequest' when calling cartPayment", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {
                "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {
                "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[]{};

        ParameterizedTypeReference<CartPayment200Response> localVarReturnType = new ParameterizedTypeReference<CartPayment200Response>() {
        };
        return apiClient.invokeAPI("/payment", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Оплата заказа
     *
     * <p><b>200</b> - Платёж успешно выполнен
     * <p><b>400</b> - Недостаточно средств
     * <p><b>404</b> - Пользователь не найден
     *
     * @param cartPaymentRequest The cartPaymentRequest parameter
     * @return CartPayment200Response
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<CartPayment200Response> cartPayment(CartPaymentRequest cartPaymentRequest) throws WebClientResponseException {
        ParameterizedTypeReference<CartPayment200Response> localVarReturnType = new ParameterizedTypeReference<CartPayment200Response>() {
        };
        return cartPaymentRequestCreation(cartPaymentRequest).bodyToMono(localVarReturnType);
    }

    /**
     * Оплата заказа
     *
     * <p><b>200</b> - Платёж успешно выполнен
     * <p><b>400</b> - Недостаточно средств
     * <p><b>404</b> - Пользователь не найден
     *
     * @param cartPaymentRequest The cartPaymentRequest parameter
     * @return ResponseEntity&lt;CartPayment200Response&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<CartPayment200Response>> cartPaymentWithHttpInfo(CartPaymentRequest cartPaymentRequest) throws WebClientResponseException {
        ParameterizedTypeReference<CartPayment200Response> localVarReturnType = new ParameterizedTypeReference<CartPayment200Response>() {
        };
        return cartPaymentRequestCreation(cartPaymentRequest).toEntity(localVarReturnType);
    }

    /**
     * Оплата заказа
     *
     * <p><b>200</b> - Платёж успешно выполнен
     * <p><b>400</b> - Недостаточно средств
     * <p><b>404</b> - Пользователь не найден
     *
     * @param cartPaymentRequest The cartPaymentRequest parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec cartPaymentWithResponseSpec(CartPaymentRequest cartPaymentRequest) throws WebClientResponseException {
        return cartPaymentRequestCreation(cartPaymentRequest);
    }

    /**
     * Получение баланса
     *
     * <p><b>200</b> - Успешный ответ
     * <p><b>404</b> - Пользователь не найден
     *
     * @param userId Идентификатор пользователя
     * @return GetBalanceById200Response
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getBalanceByIdRequestCreation(String userId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new WebClientResponseException("Missing the required parameter 'userId' when calling getBalanceById", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("userId", userId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {
                "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {};
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[]{};

        ParameterizedTypeReference<GetBalanceById200Response> localVarReturnType = new ParameterizedTypeReference<GetBalanceById200Response>() {
        };
        return apiClient.invokeAPI("/balance/{userId}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Получение баланса
     *
     * <p><b>200</b> - Успешный ответ
     * <p><b>404</b> - Пользователь не найден
     *
     * @param userId Идентификатор пользователя
     * @return GetBalanceById200Response
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<GetBalanceById200Response> getBalanceById(String userId) throws WebClientResponseException {
        ParameterizedTypeReference<GetBalanceById200Response> localVarReturnType = new ParameterizedTypeReference<GetBalanceById200Response>() {
        };
        return getBalanceByIdRequestCreation(userId).bodyToMono(localVarReturnType);
    }

    /**
     * Получение баланса
     *
     * <p><b>200</b> - Успешный ответ
     * <p><b>404</b> - Пользователь не найден
     *
     * @param userId Идентификатор пользователя
     * @return ResponseEntity&lt;GetBalanceById200Response&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<GetBalanceById200Response>> getBalanceByIdWithHttpInfo(String userId) throws WebClientResponseException {
        ParameterizedTypeReference<GetBalanceById200Response> localVarReturnType = new ParameterizedTypeReference<GetBalanceById200Response>() {
        };
        return getBalanceByIdRequestCreation(userId).toEntity(localVarReturnType);
    }

    /**
     * Получение баланса
     *
     * <p><b>200</b> - Успешный ответ
     * <p><b>404</b> - Пользователь не найден
     *
     * @param userId Идентификатор пользователя
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getBalanceByIdWithResponseSpec(String userId) throws WebClientResponseException {
        return getBalanceByIdRequestCreation(userId);
    }
}
