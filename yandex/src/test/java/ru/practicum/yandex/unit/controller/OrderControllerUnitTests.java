package ru.practicum.yandex.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.orderService.OrderService;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerUnitTests {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    OrderService orderService;
    @MockitoBean
    CartService cartService;

    @BeforeEach
    public void setUp(){
        reset(orderService,cartService);
    }

    @Test
    void test_getOrders() throws Exception {
        when(orderService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(status().isOk());
    }

    @Test
    void test_getOrder() throws Exception {
        OrderWithItems orderWithItems = new OrderWithItems();
        orderWithItems.setId(1);
        orderWithItems.setItems(new ArrayList<>());
        orderWithItems.setTotalSum(1.0);
        when(orderService.findById(1)).thenReturn(Optional.of(orderWithItems));
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    void test_buy() throws Exception {
        Cart cart = new Cart();
        cart.setItems(new HashSet<>());
        when(cartService.getCartById(1)).thenReturn(Optional.of(cart));
        doNothing().when(orderService).createOrder(cart);
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/buy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/orders?newOrder=true"));

    }
}
