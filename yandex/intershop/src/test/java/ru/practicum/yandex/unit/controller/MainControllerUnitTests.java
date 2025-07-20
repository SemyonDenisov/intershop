package ru.practicum.yandex.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;
import ru.practicum.yandex.service.orderService.OrderService;
import ru.practicum.yandex.service.paymentService.PaymentService;


import java.util.Arrays;

import static org.mockito.Mockito.*;

@WebFluxTest
public class MainControllerUnitTests {
    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    ItemService itemService;
    @MockitoBean
    CartService cartService;
    @MockitoBean
    OrderService orderService;
    @MockitoBean
    PaymentService paymentService;

    @BeforeEach
    void setUp() {
        reset(cartService, itemService, orderService);
    }

    @Test
    void test_getItems() throws Exception {
        Item item = new Item();
        item.setId(1);
        item.setDescription("This is a test item");
        item.setPrice(3.0);
        item.setImgPath("");
        item.setCount(1);
        when(itemService.findAll(3,
                1,
                "test",
                "NO")
        ).then(invocation -> {
            System.out.println("ARGS: " + Arrays.toString(invocation.getArguments()));
            return Flux.just(item);
        });
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/main/items")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void test_changeCountOfItem() throws Exception {
        var builder = new MultipartBodyBuilder();
        builder.part("action", "plus");
        when(cartService.changeCart(1, "plus")).thenReturn(Mono.empty());
        webTestClient.post()
                .uri("/main/items/1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/main/items");
        verify(cartService, times(1)).changeCart(1, "plus");
    }

}
