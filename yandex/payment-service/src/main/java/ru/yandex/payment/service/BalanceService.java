package ru.yandex.payment.service;

import reactor.core.publisher.Mono;
import ru.yandex.payment.model.Action;
import ru.yandex.payment.model.CartPaymentResponse;


public interface BalanceService {
    Mono<Double> getBalanceByUserId(Integer userId);

    Mono<CartPaymentResponse> changeBalance(Integer userId, Double amount, Action action);
}
