package ru.yandex.payment.unit.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.payment.api.BalanceApiController;
import ru.yandex.payment.service.BalanceService;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = BalanceApiController.class)
public class BalanceApiControllerUnitTests {
    @MockitoBean
    BalanceService balanceService;
    @Autowired
    private WebTestClient webClient;

    @Test
    void test_getBalance() {
        when(balanceService.getBalanceByUserId(1)).thenReturn(Mono.just(1000.0));
        webClient.get().uri("/balance/1")
                .exchange()
                .expectStatus().isOk();
    }


}
