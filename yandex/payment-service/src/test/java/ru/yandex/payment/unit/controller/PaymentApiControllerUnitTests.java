package ru.yandex.payment.unit.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;

import ru.yandex.payment.model.CartPaymentRequest;
import ru.yandex.payment.model.CartPaymentResponse;
import ru.yandex.payment.service.BalanceService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static ru.yandex.payment.model.Action.MINUS;

@WebFluxTest
public class PaymentApiControllerUnitTests {
    @MockitoBean
    BalanceService balanceService;
    @Autowired
    private WebTestClient webClient;

    @Test
    void test_payment() {
        CartPaymentRequest request = new CartPaymentRequest(1, 100.0);
        CartPaymentResponse response = new CartPaymentResponse("OK", 50.0);
        when(balanceService.changeBalance(eq(1), eq(100.0), eq(MINUS))).thenReturn(Mono.just(response));

        webClient.post()
                .uri("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CartPaymentResponse.class)
                .value(body -> {
                    assert body != null;
                    assert "OK".equals(body.getStatus());
                    assert body.getNewBalance() == 50.0;
                });
    }

}
