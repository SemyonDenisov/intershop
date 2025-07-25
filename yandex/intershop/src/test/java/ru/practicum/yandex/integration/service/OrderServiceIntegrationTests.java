package ru.practicum.yandex.integration.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.dao.OrderItemRepository;
import ru.practicum.yandex.dto.OrderWithItems;
import ru.practicum.yandex.integration.BaseIntegrationServiceTests;
import ru.practicum.yandex.model.*;
import ru.practicum.yandex.security.model.User;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceIntegrationTests extends BaseIntegrationServiceTests {

    @BeforeEach
    public void setUp() {
        Item it1 = itemsRepository.save(new Item("title2", "description2", 2.0, 1, "")).block();
        Item it2 = itemsRepository.save(new Item("title3", "description3", 3.0, 1, "")).block();
        Cart cart = new Cart();
        cart.setInfo("info");
        cart = cartRepository.save(cart).block();

        User user = new User();
        user.setUsername("senja");
        user.setPassword("password");
        user.setCartId(cart.getId());
        user.setCartId(cart.getId());
        userRepository.save(user).block();
        cart.setUserId(user.getId());
        cartRepository.save(cart).block();

        Order order2 = new Order();
        order2.setUserId(user.getId());
        order2.setTotalSum(1.0);
        orderRepository.save(order2).block();
        Order order1 = new Order();
        order1.setUserId(user.getId());
        order1.setTotalSum(2.0);
        orderRepository.save(order1).block();


        cartItemRepository.save(new CartItem(cart.getId(), it1.getId())).block();
        cartItemRepository.save(new CartItem(cart.getId(), it2.getId())).block();
        OrderItem orderItem1 = new OrderItem(order1.getId(), it1.getId(), 1);
        OrderItem orderItem2 = new OrderItem(order2.getId(), it2.getId(), 1);
        orderItemRepository.save(orderItem1).block();
        orderItemRepository.save(orderItem2).block();
    }

    @Test
    @WithMockUser(username = "senja")
    void test_findById() {
        OrderWithItems order = orderService
                .findById(orderService.findAll("senja").collectList().block().get(0).getId(), "senja").block();
        assertEquals(1.0, order.getTotalSum());
    }

    @Test
    @WithMockUser(username = "senja")
    void test_findAll() {
        List<OrderWithItems> orders = orderService.findAll("senja").collectList().block();
        assertEquals(2, orders.size());
    }

    @Test
    @WithMockUser(username = "senja")
    void test_createOrder() {
        Mockito.when(paymentService.makeOrder(eq("senja"), any())).thenReturn(Mono.empty());
        orderService.createOrder("senja").block();
        assertEquals(3, orderRepository.findAll().collectList().block().size());
    }

}
