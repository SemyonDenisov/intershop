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
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MainControllerIntegrationTests extends BaseIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;
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
        cartRepository.save(cart);
    }

    @Test
    void test_getItems() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/main/items")
                        .queryParam("search","t")
                        .queryParam("pageSize","2")
                        .queryParam("pageNumber","1")
                        .queryParam("sort","NO"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andExpect(xpath("//table//table//tr[2]/td[1]/b").string("title1"));
    }

    @Test
    void test_changeCountOfItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/main/items/1")
                        .queryParam("action", "plus"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/main/items"));
    }

}
