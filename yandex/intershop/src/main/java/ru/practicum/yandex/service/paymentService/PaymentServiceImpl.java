package ru.practicum.yandex.service.paymentService;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Order;
import ru.yandex.payment.client.api.DefaultApi;
import ru.yandex.payment.client.model.CartPayment200Response;
import ru.yandex.payment.client.model.CartPaymentRequest;
import ru.yandex.payment.client.model.GetBalanceById200Response;

import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {

    DefaultApi api;

    public PaymentServiceImpl(DefaultApi api) {
        this.api = api;
    }

    @Override
    public Mono<Double> getBalance() {
        return api
                .getBalanceById("1").mapNotNull(GetBalanceById200Response::getBalance)
                .onErrorMap(throwable -> new RuntimeException("payment server not available"));
    }

    @Override
    public Mono<CartPayment200Response> makeOrder(Order order) {
        return api.cartPayment(new CartPaymentRequest(1, order.getTotalSum()))
                .flatMap(answer -> {
                    if (Objects.equals(answer.getStatus(), "FAILED")) {
                        return Mono.error(new RuntimeException("not enough money"));
                    } else {
                        return Mono.just(answer);
                    }
                });
    }

}
