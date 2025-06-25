package ru.practicum.yandex.integration.service;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.integration.BaseIntegrationTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cartService.CartService;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
public class CartServiceIntegrationTests extends BaseIntegrationTests {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @BeforeEach
    public void setUp() {
        cartRepository.deleteAll();
        itemsRepository.deleteAll();
        Item it1 = itemsRepository.save(new Item("title2","description2",2.0,1,""));
        Item it2 = itemsRepository.save(new Item("title3","description3",3.0,1,""));
        Cart cart = new Cart();
        cart.getItems().add(it1);
        cart.getItems().add(it2);
        cartRepository.save(cart);
    }

    @Test
    @Transactional
    void test_findById(){
        Cart cart = cartService.getCartById(cartRepository.findAll().get(0).getId()).get();
        assertEquals(5.0,cart.getTotal());
    }

    @Test
    @Transactional
    void test_changeCart(){
        int id = itemsRepository.findAll().get(0).getId();
        cartService.changeCart(id,"plus");
        Item item = itemsRepository.findById(id).get();
        assertEquals(2,item.getCount());
    }
}
