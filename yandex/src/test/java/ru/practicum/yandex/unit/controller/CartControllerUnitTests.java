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
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.service.cartService.CartService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerUnitTests {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CartService cartService;

    @BeforeEach
    public void setUp(){
        reset(cartService);
    }

    @Test
    void test_showCart() throws Exception {
        when(cartService.getCartById(1)).thenReturn(Optional.of(new Cart()));
        mockMvc.perform(MockMvcRequestBuilders.get("/cart/items"))
                .andExpect(status().isOk());
    }

    @Test
    void test_addCart() throws Exception {
        doNothing().when(cartService).changeCart(1,"plus");
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/items/1")
                        .queryParam("action", "plus"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/cart/items"));
    }
}
