package ru.yandex.payment.integration.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.payment.dao.BalanceRepository;
import ru.yandex.payment.model.Balance;
import ru.yandex.payment.model.CartPaymentRequest;
import ru.yandex.payment.model.CartPaymentResponse;
import ru.yandex.payment.service.BalanceService;

import java.util.List;
import java.util.Map;

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
    @MockitoBean
    JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        balanceRepository.deleteAll().block();
        balanceRepository.save(new Balance(100.0,1)).block();
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void test_payment() {
//        String mockToken = "mock-jwt-token";
//        Jwt mockJwt = Jwt.withTokenValue(mockToken)
//                .header("alg", "none")  // Если алгоритм none, проверьте правильность настройки токена
//                .claim("resource_access", Map.of("account", Map.of("roles", List.of("USER", "ADMIN"))))
//                .build();
//
//        when(jwtDecoder.decode(mockToken)).thenReturn(mockJwt);  // Мокаем декодирование токена

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
