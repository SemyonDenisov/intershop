package ru.yandex.payment.integration.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.payment.dao.BalanceRepository;
import ru.yandex.payment.model.Balance;
import ru.yandex.payment.model.CartPaymentRequest;
import ru.yandex.payment.model.CartPaymentResponse;
import ru.yandex.payment.service.BalanceService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static ru.yandex.payment.model.Action.MINUS;

@AutoConfigureWebTestClient
@SpringBootTest
public class PaymentApiControllerIntegrationTests {

    @Autowired
    BalanceService balanceService;
    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    private WebTestClient webClient;

    @BeforeEach
    void setUp() {
        balanceRepository.deleteAll().block();
        balanceRepository.save(new Balance(100.0,1)).block();
    }

    @Test
    void test_payment() {
        CartPaymentRequest request = new CartPaymentRequest(1, 50.0);
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
                    assert "success".equals(body.getStatus());
                    assert body.getNewBalance() == 50.0;
                });
    }
}
