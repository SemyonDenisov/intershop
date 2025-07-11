package ru.practicum.yandex.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.yandex.DAO.*;
import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.integration.BaseIntegrationTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.orderService.OrderService;


import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class OrderControllerIntegrationTests extends BaseIntegrationTests {
    @Autowired
    WebTestClient webClient;

    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartItemRepository cartItemRepository;


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
    void test_getOrders(){
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

        List<Order> orderList =  orderRepository.findAll().collectList().block();
        int id = orderList.get(orderList.size()-1).getId()+1;
        webClient.post().uri("/orders/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/orders/"+id+"?newOrder=true");

    }
}
