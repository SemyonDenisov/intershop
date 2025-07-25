package ru.yandex.payment.unit.controller;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.payment.api.BalanceApiController;
import ru.yandex.payment.service.BalanceService;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = BalanceApiController.class)
public class BalanceApiControllerUnitTests {
    @MockitoBean
    BalanceService balanceService;
    @Autowired
    private WebTestClient webClient;


    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void test_getBalance() {
        when(balanceService.getBalanceByUserId(1)).thenReturn(Mono.just(1000.0));
        webClient.get().uri("/balance/1")
                .exchange()
                .expectStatus().isOk();
    }


}
