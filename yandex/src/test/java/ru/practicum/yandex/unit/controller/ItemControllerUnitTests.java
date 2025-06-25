package ru.practicum.yandex.unit.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerUnitTests {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CartService cartService;
    @MockitoBean
    ItemService itemService;

    @BeforeEach
    public void setUp(){
        reset(itemService,cartService);
    }

    @Test
    void test_getItemInfo() throws Exception {
        when(itemService.findById(1)).thenReturn(Optional.of(new Item()));
        mockMvc.perform(MockMvcRequestBuilders.get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    void test_addItemToCart() throws Exception {
        doNothing().when(cartService).changeCart(1,"plus");
        mockMvc.perform(MockMvcRequestBuilders.post("/items/1")
                .queryParam("action","plus"))
                .andExpect(status().is3xxRedirection())
                .andExpect(
                        header().stringValues(HttpHeaders.LOCATION, "/items/1")
                );
    }

    @Test
    void test_addItem() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test".getBytes());
        doNothing().when(itemService).addItem("","",1.0,file);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/items/add")                        .file(file)
                        .part(new MockPart("title", "".getBytes()))
                        .file(file)
                        .part(new MockPart("description", "".getBytes()))
                        .part(new MockPart("price", "1.0".getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/main/items"));;
    }
}