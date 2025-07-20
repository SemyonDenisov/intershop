package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DAO.CartItemRepository;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.paymentService.PaymentService;
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
