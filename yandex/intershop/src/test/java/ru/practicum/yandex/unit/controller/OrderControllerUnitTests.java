package ru.practicum.yandex.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.dto.OrderWithItems;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;
import ru.practicum.yandex.service.orderService.OrderService;
import ru.practicum.yandex.service.paymentService.PaymentService;


import java.util.ArrayList;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest
public class OrderControllerUnitTests {
    @Autowired
    WebTestClient webClient;
    @MockitoBean
    OrderService orderService;
    @MockitoBean
    CartService cartService;

    @MockitoBean
    ItemService itemService;

    @MockitoBean
    PaymentService paymentService;
    @MockitoBean
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        reset(orderService, cartService, itemService, paymentService);
    }

    @Test
    @WithMockUser(username = "senja", roles = {"USER"})
    void test_getOrders() throws Exception {
        when(orderService.findAll("senja")).thenReturn(Flux.just());
        webClient.get().uri("/orders").exchange().expectStatus().isOk();
    }

    @Test
    @WithMockUser(username = "senja", roles = {"USER"})
    void test_getOrder() throws Exception {
        OrderWithItems orderWithItems = new OrderWithItems();
        orderWithItems.setId(1);
        orderWithItems.setItems(new ArrayList<>());
        orderWithItems.setTotalSum(1.0);
        when(orderService.findById(1, "senja")).thenReturn(Mono.just(orderWithItems));
        webClient.get().uri("/orders/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "text/html");
    }

    @Test
    @WithMockUser(username = "senja", roles = {"USER"})
    void test_buy() throws Exception {
        Cart cart = new Cart();
        cart.setItems(new HashSet<>());
        when(cartService.getCartByUsername("senja")).thenReturn(Mono.just(cart));
        when(orderService.createOrder("senja")).thenReturn(Mono.just(new Order()));
        webClient.mutateWith(csrf()).post().uri("/orders/buy")
                .exchange().expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/orders/0?newOrder=true");
    }
}
