package ru.practicum.yandex.integration.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.integration.BaseIntegrationTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CartControllerIntegrationTests extends BaseIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    CartRepository cartRepository;

    @BeforeEach
    public void setUp() {
        itemsRepository.deleteAll();
        cartRepository.deleteAll();
        Item item = new Item("title1","description1",1.0,0,"");
        itemsRepository.save(item);
        Cart cart = new Cart();
        cart.getItems().add(item);
    }

    @Test
    void test_showCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cart/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("total"))
                .andExpect(xpath("//table/tr[1]/td/b").string("Итого: 0.0 руб."));;
    }

    @Test
    void test_addToCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/items/1")
                        .queryParam("action", "plus"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/cart/items"));
    }

}
