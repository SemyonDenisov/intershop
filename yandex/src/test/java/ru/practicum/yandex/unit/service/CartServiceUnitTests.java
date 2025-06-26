package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cartService.CartService;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class CartServiceUnitTests {
    @Autowired
    private CartService cartService;
    @MockitoBean
    private CartRepository cartRepository;

    @MockitoBean
    private ItemsRepository itemsRepository;

    @BeforeEach
    public void setUp() {
        reset(itemsRepository);
        reset(cartRepository);
    }

    @Test
    void test_findById(){
        when(cartRepository.findById(1)).thenReturn(Optional.of(new Cart()));
        cartService.getCartById(1);
        verify(cartRepository, times(1)).findById(1);
    }

    @Test
    void test_changeCart(){
        when(cartRepository.findById(1)).thenReturn(Optional.of(new Cart()));
        when(itemsRepository.findById(1)).thenReturn(Optional.of(new Item()));
        cartService.changeCart(1,"plus");
        verify(itemsRepository, times(1)).findById(1);
        verify(cartRepository, times(1)).findById(1);
    }

}
