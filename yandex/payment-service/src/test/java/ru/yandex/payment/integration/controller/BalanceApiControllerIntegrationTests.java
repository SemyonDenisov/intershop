package ru.yandex.payment.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.payment.dao.BalanceRepository;
import ru.yandex.payment.model.Balance;
import ru.yandex.payment.service.BalanceService;


@AutoConfigureWebTestClient
@SpringBootTest
public class BalanceApiControllerIntegrationTests {
    @Autowired
    BalanceService balanceService;
    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    private WebTestClient webClient;

    @BeforeEach
    void setUp() {
        balanceRepository.deleteAll().block();
        balanceRepository.save(new Balance(5.0,1)).block();
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void test_getBalance() {
        webClient.get().uri("/balance/1")
                .exchange()
                .expectStatus().isOk();
    }

}
