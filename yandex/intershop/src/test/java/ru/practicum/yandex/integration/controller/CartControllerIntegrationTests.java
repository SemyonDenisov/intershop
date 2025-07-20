package ru.practicum.yandex.integration.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.practicum.yandex.integration.BaseIntegrationControllerTests;
import ru.practicum.yandex.integration.BaseIntegrationServiceTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class CartControllerIntegrationTests extends BaseIntegrationControllerTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @BeforeEach
    public void setUp() {
        Item item = new Item("title1", "description1", 1.0, 0, "");
        item = itemsRepository.save(item).block();
        Cart cart = new Cart();
        cart = cartRepository.save(cart).block();
        cartItemRepository.save(new CartItem(cart.getId(), item.getId())).block();
    }

    @Test
    void test_showCart() {
        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_addToCart() {
        var builder = new MultipartBodyBuilder();
        builder.part("action", "plus");
        List<Item> items = itemsRepository.findAll().collectList().block();
        int lastId = items.get(items.size() - 1).getId();
        webTestClient.post()
                .uri("/cart/items/" + lastId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/cart/items");
        assertThat(redisTemplate.opsForValue().get("item:" + lastId) instanceof Item).isTrue();
    }

}
