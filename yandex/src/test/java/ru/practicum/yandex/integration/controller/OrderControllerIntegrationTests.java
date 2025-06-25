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
import ru.practicum.yandex.DAO.OrderRepository;
import ru.practicum.yandex.integration.BaseIntegrationTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.orderService.OrderService;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderControllerIntegrationTests extends BaseIntegrationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;

    @BeforeEach
    public void setUp() {
        itemsRepository.deleteAll();
        cartRepository.deleteAll();
        orderRepository.deleteAll();
        Item item = new Item("title1","description1",1.0,1,"");
        itemsRepository.save(item);
        Cart cart = new Cart();
        cart.getItems().add(item);
        cartRepository.save(cart);
        orderService.createOrder(cart);
    }

    @Test
    void test_getOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(xpath("//table/tr[1]/td[1]/p/b").string("Сумма: 1.0 руб."));;;
    }

    @Test
    void test_getOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    void test_buy() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/buy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/orders?newOrder=true"));

    }
}
