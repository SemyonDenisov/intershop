package ru.practicum.yandex.service.PaymentService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Order;
import ru.yandex.payment.client.api.DefaultApi;
import ru.yandex.payment.client.model.CartPayment200Response;
import ru.yandex.payment.client.model.CartPaymentRequest;
import ru.yandex.payment.client.model.GetBalanceById200Response;

@Service
public class PaymentServiceImpl implements PaymentService {

    DefaultApi api;

    public PaymentServiceImpl() {
        this.api = new DefaultApi();
    }

    @Override
    public Mono<Double> getBalance() {
        return api
                .getBalanceById("1").mapNotNull(GetBalanceById200Response::getBalance);
    }

    @Override
    public Mono<CartPayment200Response> makeOrder(Order order) {
        return api.getBalanceById("1").doOnNext(ans -> System.out.println("111")).then(api.cartPayment(new CartPaymentRequest()));
    }

}
