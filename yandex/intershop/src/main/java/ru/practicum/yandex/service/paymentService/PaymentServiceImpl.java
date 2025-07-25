package ru.practicum.yandex.service.paymentService;


import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.security.model.User;
import ru.yandex.payment.client.api.DefaultApi;
import ru.yandex.payment.client.model.CartPayment200Response;
import ru.yandex.payment.client.model.CartPaymentRequest;
import ru.yandex.payment.client.model.GetBalanceById200Response;

import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final ReactiveOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    DefaultApi api;

    private final UserRepository userRepository;

    public PaymentServiceImpl(DefaultApi api, UserRepository userRepository, ReactiveOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        this.api = api;
        this.userRepository = userRepository;
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
    }

    @Override
    @PreAuthorize("#username == authentication.name")
    public Mono<Double> getBalance(String username) {
        return getToken().flatMap(token -> userRepository.findByUsername(username)
                .map(User::getId)
                .flatMap(userId -> api.getBalanceById(userId.toString(), token)
                        .mapNotNull(GetBalanceById200Response::getBalance)
                        .onErrorMap(throwable -> new RuntimeException("payment server not available"))));

    }

    @Override
    @PreAuthorize("#username == authentication.name")
    public Mono<CartPayment200Response> makeOrder(String username, Order order) {
        return userRepository.findByUsername(username).map(User::getId)
                .flatMap(userId -> getToken()
                        .flatMap(token -> api.cartPayment(new CartPaymentRequest(userId, order.getTotalSum()), token)
                                .flatMap(answer -> {
                                    if (Objects.equals(answer.getStatus(), "FAILED")) {
                                        return Mono.error(new RuntimeException("not enough money"));
                                    } else {
                                        return Mono.just(answer);
                                    }
                                })));
    }

    private Mono<String> getToken() {
        return oAuth2AuthorizedClientManager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("intershop")
                        .principal("system")
                        .build())
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2AccessToken::getTokenValue);
    }

}
