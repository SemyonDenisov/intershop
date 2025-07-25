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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.practicum.yandex.integration.BaseIntegrationControllerTests;
import ru.practicum.yandex.integration.BaseIntegrationServiceTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.security.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class MainControllerIntegrationTests extends BaseIntegrationControllerTests {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @BeforeEach
    public void setUp() {
        Item item = new Item("title1", "description1", 1.0, 0, "");
        item = itemsRepository.save(item).block();
        Cart cart = new Cart();
        cart.setInfo("info");
        cart = cartRepository.save(cart).block();
        User user = new User();
        user.setUsername("senja");
        user.setPassword("password");
        user.setCartId(cart.getId());
        userRepository.save(user).block();
        cart.setUserId(user.getId());
        cartRepository.save(cart).block();
        cartItemRepository.save(new CartItem(cart.getId(), item.getId())).block();
    }

    @Test
    @WithMockUser(username = "senja",roles={"USER"})
    void test_getItems() throws Exception {
        webTestClient.get()
                .uri("/main/items")
                .exchange()
                .expectStatus().isOk();
        assertThat(((List<Item>) redisTemplate.opsForValue().get("items:no::0:10:list")).size()).isEqualTo(1);
    }

    @Test
    @WithMockUser(username="senja",roles = {"USER"})
    void test_changeCountOfItem() throws Exception {
        var builder = new MultipartBodyBuilder();
        builder.part("action", "plus");
        List<Item> items = itemsRepository.findAll().collectList().block();
        int lastId = items.get(items.size() - 1).getId();
        webTestClient.mutateWith(csrf()).post()
                .uri("/main/items/" + lastId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/main/items");
        assertThat(redisTemplate.opsForValue().get("item:" + lastId) instanceof Item).isTrue();
    }

}
