package ru.practicum.yandex.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;
import ru.practicum.yandex.service.orderService.OrderService;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @BeforeEach
    public void setUp() {
        reset(orderService, cartService,itemService);
    }

    @Test
    void test_getOrders() throws Exception {
        when(orderService.findAll()).thenReturn(Flux.just());
        webClient.get().uri("/orders").exchange().expectStatus().isOk();
    }

    @Test
    void test_getOrder() throws Exception {
        OrderWithItems orderWithItems = new OrderWithItems();
        orderWithItems.setId(1);
        orderWithItems.setItems(new ArrayList<>());
        orderWithItems.setTotalSum(1.0);
        when(orderService.findById(1)).thenReturn(Mono.just(orderWithItems));
        webClient.get().uri("/orders/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "text/html");
    }

    @Test
    void test_buy() throws Exception {
        Cart cart = new Cart();
        cart.setItems(new HashSet<>());
        when(cartService.getCartById(1)).thenReturn(Mono.just(cart));
        when(orderService.createOrder(cart)).thenReturn(Mono.just(new Order()));
        webClient.post().uri("/orders/buy")
                .exchange().expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/orders/0?newOrder=true");
    }
}
