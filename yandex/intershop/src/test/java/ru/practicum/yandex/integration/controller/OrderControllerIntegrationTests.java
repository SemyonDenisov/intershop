package ru.practicum.yandex.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.integration.BaseIntegrationControllerTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.security.model.User;
import ru.yandex.payment.client.model.CartPayment200Response;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class OrderControllerIntegrationTests extends BaseIntegrationControllerTests {
    @Autowired
    WebTestClient webClient;


    @BeforeEach
    public void setUp() {
        Item item = new Item("title1", "description1", 1.0, 1, "");
        item = itemsRepository.save(item).block();

        Cart cart = new Cart();
        cart.setInfo("info");
        cart = cartRepository.save(cart).block();
        User user = new User();
        user.setUsername("senja");
        user.setPassword("password");
        user.setCartId(cart.getId());
        userRepository.save(user).block();
        cart.setUserId(user.getId());
        cartRepository.save(cart).block();

        cartItemRepository.save(new CartItem(cart.getId(), item.getId())).block();
        CartPayment200Response cr = new CartPayment200Response();
        when(paymentService.makeOrder(eq("senja"), any())).thenReturn(Mono.just(cr));
        Order order = orderService.createOrder("senja").block();

    }

    @Test
    @WithMockUser(username = "senja", roles = {"USER"})
    void test_getOrders() {
        webClient.get().uri("/orders/{id}", orderRepository.findAll().collectList().block().get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "text/html");
    }

    @Test
    @WithMockUser(username = "senja", roles = {"USER"})
    void test_getOrder() {
        webClient.get().uri("/orders").exchange().expectStatus().isOk();
    }

    @Test
    @WithMockUser(username = "senja", roles = {"USER"})
    void test_buy() {
        List<Order> orderList = orderRepository.findAll().collectList().block();
        int id = orderList.get(orderList.size() - 1).getId() + 1;
        webClient.mutateWith(csrf()).post().uri("/orders/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/orders/" + id + "?newOrder=true");

    }
}
