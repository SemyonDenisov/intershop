package ru.practicum.yandex.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;
import ru.practicum.yandex.service.orderService.OrderService;
import ru.practicum.yandex.service.paymentService.PaymentService;


import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest
public class CartControllerUnitTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    CartService cartService;
    @MockitoBean
    ItemService itemService;
    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    OrderService orderService;
    @MockitoBean
    PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        reset(cartService, itemService);
    }

    @Test
    @WithMockUser(username = "senja",roles={"USER"})
    void test_showCart() throws Exception {
        Cart cart = new Cart();
        Item item = new Item();
        item.setPrice(3.0);
        item.setCount(1);
        cart.setItems(Set.of(item));
        when(cartService.getCartByUsername("senja")).thenReturn(Mono.just(cart));
        when(paymentService.getBalance("senja")).thenReturn(Mono.just(10.0));
        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser(username = "senja",roles={"USER"})
    void test_addCart() throws Exception {
        var builder = new MultipartBodyBuilder();
        builder.part("action", "plus");
        when(cartService.changeCart(1, "plus","senja")).thenReturn(Mono.empty());
        when(paymentService.getBalance("senja")).thenReturn(Mono.just(10.0));
        webTestClient.mutateWith(csrf()).post()
                .uri("/cart/items/1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/cart/items");
        verify(cartService, times(1)).changeCart(1, "plus","senja");

    }
}
