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
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class MainControllerIntegrationTests extends BaseIntegrationTests {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;
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
    void test_getItems() throws Exception {
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
        webTestClient.post()
                .uri("/main/items/1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/main/items");
    }

}
