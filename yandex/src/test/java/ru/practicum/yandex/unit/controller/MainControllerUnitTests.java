package ru.practicum.yandex.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;
import ru.practicum.yandex.unit.service.ItemServiceUnitTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerUnitTests {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ItemService itemService;
    @MockitoBean
    CartService cartService;

    @BeforeEach
    void setUp() {
        reset(cartService, itemService);
    }

    @Test
    void test_getItems() throws Exception {
        when(itemService.findAll(PageRequest.of(0,2, Sort.by(Sort.Direction.DESC, "id")),"t"))
                .thenReturn(Page.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/main/items")
                        .queryParam("search","t")
                        .queryParam("pageSize","2")
                        .queryParam("pageNumber","1")
                        .queryParam("sort","NO"))
                .andExpect(status().isOk());
    }

    @Test
    void test_changeCountOfItem() throws Exception {
        doNothing().when(cartService).changeCart(1,"plus");
        mockMvc.perform(MockMvcRequestBuilders.post("/main/items/1")
                        .queryParam("action", "plus"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/main/items"));
    }

}
