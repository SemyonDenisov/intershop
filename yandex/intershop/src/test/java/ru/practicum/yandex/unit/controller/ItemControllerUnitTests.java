package ru.practicum.yandex.unit.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;
import ru.practicum.yandex.service.orderService.OrderService;
import ru.practicum.yandex.service.paymentService.PaymentService;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@WebFluxTest
public class ItemControllerUnitTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    CartService cartService;
    @MockitoBean
    ItemService itemService;
    @MockitoBean
    OrderService orderService;
    @MockitoBean
    PaymentService paymentService;
    @MockitoBean
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        reset(itemService, cartService);
    }

    @Test
    @WithMockUser(username = "senja")
    void test_getItemInfo() throws Exception {
        Item item = new Item();
        item.setCount(1);
        item.setPrice(3.0);
        item.setDescription("This is a test");
        when(itemService.findById(1)).thenReturn(Mono.just(item));
        webTestClient.get().uri("/items/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML_VALUE);
        verify(itemService, times(1)).findById(1);
    }

    @Test
    @WithMockUser(username = "senja",roles={"USER"})
    void test_addItemToCart() throws Exception {
        when(cartService.changeCart(1, "plus","senja")).thenReturn(Mono.empty());
        var builder = new MultipartBodyBuilder();
        builder.part("action", "plus");
        webTestClient.mutateWith(csrf()).post()
                .uri("/items/1")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/items/1");
        verify(cartService, times(1)).changeCart(1, "plus","senja");
    }

    @Test
    @WithMockUser(username = "senja",roles={"USER"})
    void test_addItem() throws Exception {
        byte[] content = "test-content".getBytes(StandardCharsets.UTF_8);

        var builder = new MultipartBodyBuilder();
        builder.part("title", "test");
        builder.part("image", new ByteArrayResource(content));
        builder.part("price", "3.0");
        builder.part("description", "This is a test");

        when(itemService.addItem(eq("test"), eq("This is a test"), eq(3.0), any())).thenReturn(Mono.empty());
        webTestClient.mutateWith(csrf()).post().uri("/items/add")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/main/items");
        verify(itemService, times(1)).addItem(eq("test"), eq("This is a test"), eq(3.0), any());
    }
}