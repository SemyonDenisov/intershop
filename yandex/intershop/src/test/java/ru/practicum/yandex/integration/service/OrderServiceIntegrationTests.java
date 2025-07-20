package ru.practicum.yandex.integration.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.integration.BaseIntegrationServiceTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.model.Order;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceIntegrationTests extends BaseIntegrationServiceTests {

    @BeforeEach
    public void setUp() {
        Item it1 = itemsRepository.save(new Item("title2", "description2", 2.0, 1, "")).block();
        Item it2 = itemsRepository.save(new Item("title3", "description3", 3.0, 1, "")).block();
        Cart cart = new Cart();
        cart = cartRepository.save(cart).block();
        orderRepository.save(new Order(3.5)).block();
        orderRepository.save(new Order(4.0)).block();
        cartItemRepository.save(new CartItem(cart.getId(), it1.getId())).block();
        cartItemRepository.save(new CartItem(cart.getId(), it2.getId())).block();
    }

    @Test
    void test_findById() {
        OrderWithItems order = orderService.findById(orderService.findAll().collectList().block().get(0).getId()).block();
        assertEquals(4.0, order.getTotalSum());
    }

    @Test
    void test_findAll() {
        List<OrderWithItems> orders = orderService.findAll().collectList().block();
        assertEquals(2, orders.size());
    }

    @Test
    void test_createOrder() {
        Mockito.when(paymentService.makeOrder(any())).thenReturn(Mono.empty());
        Cart cart = cartRepository.findById(cartRepository.findAll().collectList().block().get(0).getId()).block();
        orderService.createOrder(cart).block();
        assertEquals(3, orderRepository.findAll().collectList().block().size());
    }

}
