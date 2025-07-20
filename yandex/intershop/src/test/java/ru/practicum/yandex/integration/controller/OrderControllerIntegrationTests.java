package ru.practicum.yandex.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.practicum.yandex.integration.BaseIntegrationControllerTests;
import ru.practicum.yandex.integration.BaseIntegrationServiceTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.model.Order;


import java.util.List;


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
        cart = cartRepository.save(cart).block();
        cartItemRepository.save(new CartItem(cart.getId(), item.getId())).block();
        Order order = orderService.createOrder(cart).block();
    }

    @Test
    void test_getOrders() {
        webClient.get().uri("/orders/{id}", orderRepository.findAll().collectList().block().get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "text/html");
    }

    @Test
    void test_getOrder() {
        webClient.get().uri("/orders").exchange().expectStatus().isOk();
    }

    @Test
    void test_buy() {
        List<Order> orderList = orderRepository.findAll().collectList().block();
        int id = orderList.get(orderList.size() - 1).getId() + 1;
        webClient.post().uri("/orders/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/orders/" + id + "?newOrder=true");

    }
}
