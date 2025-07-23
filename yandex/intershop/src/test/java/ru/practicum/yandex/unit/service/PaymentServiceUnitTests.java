package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.service.paymentService.PaymentServiceImpl;
import ru.yandex.payment.client.api.DefaultApi;
import ru.yandex.payment.client.model.CartPayment200Response;
import ru.yandex.payment.client.model.GetBalanceById200Response;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentServiceUnitTests {

    @Mock
    DefaultApi mockApi;

    @InjectMocks
    PaymentServiceImpl paymentService;


    @Test
    void test_getBalance() {
        GetBalanceById200Response response = new GetBalanceById200Response();
        response.setBalance(100.0);
        when(mockApi.getBalanceById("1"))
                .thenReturn(Mono.just(response));
        paymentService.getBalance().block();
        verify(mockApi, times(1)).getBalanceById("1");
    }

    @Test
    void testMakeOrderSuccess() {
        Order order = new Order();
        order.setTotalSum(50.0);

        CartPayment200Response response = new CartPayment200Response();
        response.setStatus("OK");

        when(mockApi.cartPayment(any()))
                .thenReturn(Mono.just(response));
        paymentService.makeOrder(order).block();
        verify(mockApi, times(1)).cartPayment(any());
    }

}
