package ru.practicum.yandex.service.paymentService;

import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Order;
import ru.yandex.payment.client.model.CartPayment200Response;

public interface PaymentService {
    Mono<Double> getBalance();
    Mono<CartPayment200Response> makeOrder(Order order);
}
