package ru.practicum.yandex.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import ru.practicum.yandex.integration.BaseIntegrationControllerTests;
import ru.practicum.yandex.integration.BaseIntegrationServiceTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.security.model.User;


import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class ItemControllerIntegrationTests extends BaseIntegrationControllerTests {
    @Autowired
    WebTestClient webTestClient;
//
//    @Autowired
//    ItemsRepository itemsRepository;
//    @Autowired
//    CartRepository cartRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setUp() {
        itemsRepository.save(new Item("title1", "description1", 1.0, 0, "")).block();
        itemsRepository.save(new Item("title2", "description2", 2.0, 1, "")).block();
        itemsRepository.save(new Item("title3", "description3", 3.0, 2, "")).block();
        itemsRepository.save(new Item("title4", "description4", 4.0, 3, "")).block();
        itemsRepository.save(new Item("title5", "description5", 5.0, 4, "")).block();
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
    }

    @Test
    @WithMockUser(username = "senja", roles = {"USER"})
    void test_getItemInfo() throws Exception {
        var builder = new MultipartBodyBuilder();
        builder.part("action", "plus");
        List<Item> items = itemsRepository.findAll().collectList().block();
        int lastId = items.get(items.size() - 1).getId();
        webTestClient.mutateWith(csrf()).post()
                .uri("/items/" + lastId)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/items/" + lastId);
        assertThat(redisTemplate.opsForValue().get("item:" + lastId) instanceof Item).isTrue();
    }

    @Test
    @WithMockUser(username = "senja", roles = {"USER"})
    void test_addItemToCart() throws Exception {
        var builder = new MultipartBodyBuilder();
        builder.part("action", "plus");
        List<Item> items = itemsRepository.findAll().collectList().block();
        int lastId = items.get(items.size() - 1).getId();
        webTestClient.mutateWith(csrf()).post()
                .uri("/items/" + lastId)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/items/" + lastId);
        assertThat(redisTemplate.opsForValue().get("item:" + lastId) instanceof Item).isTrue();
    }

    @Nested
    class AddItemTest {

        @Value("${spring.image.savePath}")
        String imageStorePath;

        void cleanup() {
            List<Item> items = itemsRepository.findAll().collectList().block();
            String imagePath = items.get(items.size() - 1).getImgPath();
            new File(imageStorePath + imagePath).delete();
        }

        @Test
        @WithMockUser(username = "senja", roles = {"MODERATOR"})
        void test_addItem() throws Exception {
            try {
                ClassPathResource resource = new ClassPathResource("test.jpg");

                MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
                multipartData.add("title", "Test Item");
                multipartData.add("description", "Test description");
                multipartData.add("price", "9.99");
                multipartData.add("image", resource);
                webTestClient.mutateWith(csrf()).post()
                        .uri("/items/add")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(multipartData))
                        .exchange()
                        .expectStatus().is3xxRedirection()
                        .expectHeader().valueEquals(HttpHeaders.LOCATION, "/main/items");
                cleanup();
            } catch (Exception e) {
                throw e;
            }
        }
    }
}
