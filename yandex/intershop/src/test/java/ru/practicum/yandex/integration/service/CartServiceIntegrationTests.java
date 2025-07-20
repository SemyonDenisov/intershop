package ru.practicum.yandex.integration.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.integration.BaseIntegrationServiceTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
public class CartServiceIntegrationTests extends BaseIntegrationServiceTests {

    @BeforeEach
    @Transactional
    public void setUp() {
        Item it1 = itemsRepository.save(new Item("title2","description2",2.0,1,"")).block();
        Item it2 = itemsRepository.save(new Item("title3","description3",3.0,1,"")).block();
        Cart cart = new Cart();
        cart = cartRepository.save(cart).block();
        cartItemRepository.save(new CartItem(cart.getId(), it1.getId())).block();
        cartItemRepository.save(new CartItem(cart.getId(), it2.getId())).block();
    }

    @Test
    void test_findById(){
        Cart cart =
                cartService.getCartById(cartRepository.findAll().collectList().block().get(0).getId()).block();
        assertEquals(5.0,cart.getTotal());
    }

    @Test
    void test_changeCart(){
        int id = itemsRepository.findAll().collectList().block().get(0).getId();
        cartService.changeCart(id,"plus").block();
        Item item = itemsRepository.findById(id).block();
        assertEquals(2,item.getCount());
    }
}
