package ru.practicum.yandex.integration.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.practicum.yandex.DAO.CartItemRepository;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.integration.BaseIntegrationTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class CartControllerIntegrationTests extends BaseIntegrationTests {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;


    @BeforeEach
    public void setUp() {
        Item item = new Item("title1", "description1", 1.0, 0, "");
        item = itemsRepository.save(item).block();
        Cart cart = new Cart();
        cart = cartRepository.save(cart).block();
        cartItemRepository.save(new CartItem(cart.getId(), item.getId())).block();
    }

    @Test
    void test_showCart() throws Exception {
        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_addToCart() throws Exception {
        var builder = new MultipartBodyBuilder();
        builder.part("action", "plus");
        webTestClient.post()
                .uri("/cart/items/1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/cart/items");
    }

}
