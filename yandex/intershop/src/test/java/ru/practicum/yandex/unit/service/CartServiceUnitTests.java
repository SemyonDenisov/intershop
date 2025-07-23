package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.dao.CartItemRepository;
import ru.practicum.yandex.dao.CartRepository;
import ru.practicum.yandex.dao.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cache.itemCacheService.ItemCacheService;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.paymentService.PaymentService;


import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class CartServiceUnitTests {
    @Autowired
    private CartService cartService;
    @MockitoBean
    private CartRepository cartRepository;

    @MockitoBean
    private CartItemRepository cartItemRepository;

    @MockitoBean
    private ItemsRepository itemsRepository;

    @MockitoBean
    private PaymentService paymentService;
    @MockitoBean
    private ItemCacheService itemCacheService;

    @BeforeEach
    public void setUp() {
        reset(itemsRepository);
        reset(cartRepository);
    }

    @Test
    void test_findById() {
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setCartId(1);
        cartItem.setItemId(1);
        Item item = new Item();
        item.setId(1);
        when(cartRepository.findById(1)).thenReturn(Mono.just(cart));
        when(cartRepository.findAll()).thenReturn(Flux.just(cart));
        when(cartItemRepository.findByCartId(1)).thenReturn(Flux.just(cartItem));
        when(itemsRepository.findById(1)).thenReturn(Mono.just(item));
        cartService.getCartById(1).block();
        verify(cartRepository, times(1)).findById(1);
    }

    @Test
    void test_changeCart() {
        Item item = new Item();
        CartItem cartItem = new CartItem(1, 1);
        when(cartRepository.findById(1)).thenReturn(Mono.just(new Cart()));
        when(itemsRepository.findById(1)).thenReturn(Mono.just(item));
        when(itemsRepository.save(item)).thenReturn(Mono.just(new Item()));

        when(cartItemRepository.findByCartIdAndItemId(1, 1)).thenReturn(Mono.just(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(Mono.just(cartItem));
        cartService.changeCart(1, "plus").block();
        verify(itemsRepository, times(1)).findById(1);
    }

}
