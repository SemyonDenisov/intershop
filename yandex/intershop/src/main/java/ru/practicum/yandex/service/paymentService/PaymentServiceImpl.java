package ru.practicum.yandex.service.paymentService;



import org.springframework.security.access.prepost.PreAuthorize;
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

    DefaultApi api;

    private final UserRepository userRepository;

    public PaymentServiceImpl(DefaultApi api, UserRepository userRepository) {
        this.api = api;
        this.userRepository = userRepository;
    }

    @Override
    @PreAuthorize("#username == authentication.name")
    public Mono<Double> getBalance(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .flatMap(userId -> api.getBalanceById(userId.toString())
                        .mapNotNull(GetBalanceById200Response::getBalance)
                .onErrorMap(throwable -> new RuntimeException("payment server not available")));
    }

    @Override
    @PreAuthorize("#username == authentication.name")
    public Mono<CartPayment200Response> makeOrder(String username,Order order) {
        return userRepository.findByUsername(username).map(User::getId)
                .flatMap(userId-> api.cartPayment(new CartPaymentRequest(userId, order.getTotalSum()))
                 .flatMap(answer -> {
                     if (Objects.equals(answer.getStatus(), "FAILED")) {
                         return Mono.error(new RuntimeException("not enough money"));
                     } else {
                         return Mono.just(answer);
                     }
                 }));
    }

}
