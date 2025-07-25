package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.security.model.User;
import ru.practicum.yandex.service.paymentService.PaymentServiceImpl;
import ru.yandex.payment.client.api.DefaultApi;
import ru.yandex.payment.client.model.CartPayment200Response;
import ru.yandex.payment.client.model.GetBalanceById200Response;

import java.time.Instant;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentServiceUnitTests {

    @Mock
    DefaultApi mockApi;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @MockitoBean
    ReactiveOAuth2AuthorizedClientManager manager;
    @MockitoBean
    UserRepository userRepository;


    @Test
    @WithMockUser(username = "senja")
    void test_getBalance() {

        OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "ggggacsdccasc", Instant.MIN, Instant.MAX);
        ClientRegistration registration = ClientRegistration.withRegistrationId("intershop")
                .clientName("senja").clientId("senja").redirectUri("asdasd")
                .authorizationUri("asas").tokenUri("ada")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).build();
        OAuth2AuthorizedClient client = new OAuth2AuthorizedClient(registration, "senja", token);

        when(manager.authorize(any())).thenReturn(Mono.just(client));
        GetBalanceById200Response response = new GetBalanceById200Response();
        response.setBalance(100.0);
        when(mockApi.getBalanceById(any(), any())).thenReturn(Mono.just(response));
        User user = new User();
        user.setId(1);
        when(userRepository.findByUsername("senja")).thenReturn(Mono.just(user));
        paymentService.getBalance("senja").block();
        verify(mockApi, times(1)).getBalanceById(any(), any());
    }

    @Test
    @WithMockUser(username = "senja")
    void testMakeOrderSuccess() {
        OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "ggggacsdccasc", Instant.MIN, Instant.MAX);
        ClientRegistration registration = ClientRegistration.withRegistrationId("intershop")
                .clientName("senja").clientId("senja").redirectUri("asdasd")
                .authorizationUri("asas").tokenUri("ada")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).build();
        OAuth2AuthorizedClient client = new OAuth2AuthorizedClient(registration, "senja", token);
        Order order = new Order();
        order.setTotalSum(50.0);

        CartPayment200Response response = new CartPayment200Response();
        response.setStatus("OK");

        when(manager.authorize(any())).thenReturn(Mono.just(client));
        User user = new User();
        user.setId(1);
        when(userRepository.findByUsername("senja")).thenReturn(Mono.just(user));

        when(mockApi.cartPayment(any(),any())).thenReturn(Mono.empty());
        paymentService.makeOrder("senja", order).block();
        verify(mockApi, times(1)).cartPayment(any(), any());
    }

}
