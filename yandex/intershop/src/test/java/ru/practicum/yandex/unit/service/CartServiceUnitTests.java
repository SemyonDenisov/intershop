package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
import ru.practicum.yandex.security.dao.RoleRepository;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.security.dao.UserRoleRepository;
import ru.practicum.yandex.security.model.Role;
import ru.practicum.yandex.security.model.User;
import ru.practicum.yandex.service.cache.itemCacheService.ItemCacheService;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.paymentService.PaymentService;


import java.util.List;

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

    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private RoleRepository roleRepository;
    @MockitoBean
    private UserRoleRepository userRoleRepository;


    @BeforeEach
    public void setUp() {
        reset(itemsRepository);
        reset(cartRepository);
    }

    @Test
    @WithMockUser(username = "senja")
    void test_findById() {
        User user = new User();
        Role role = new Role();
        role.setRolename("USER");
        user.setRoles(List.of(role));
        user.setCartId(1);
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setCartId(1);
        cartItem.setItemId(1);
        cartItem.setCount(0);
        Item item = new Item();
        item.setId(1);
        when(cartRepository.findById(any(Integer.class))).thenReturn(Mono.just(cart));
        when(cartRepository.findAll()).thenReturn(Flux.just(cart));
        when(cartItemRepository.findByCartId(any())).thenReturn(Flux.just(cartItem));
        when(itemsRepository.findById(any(Integer.class))).thenReturn(Mono.just(item));
        when(userRepository.findByUsername("senja")).thenReturn(Mono.just(user));
        cartService.getCartByUsername("senja").block();
        verify(cartRepository, times(1)).findById(1);
    }

    @Test
    @WithMockUser(username = "senja")
    void test_changeCart() {
        Item item = new Item();
        CartItem cartItem = new CartItem(1, 1);
        cartItem.setCount(1);
        User user = new User();
        Role role = new Role();
        role.setRolename("USER");
        user.setRoles(List.of(role));
        user.setUsername("senja");
        user.setCartId(1);
        user.setCartId(1);
        Cart cart = new Cart();
        cart.setUserId(1);
        when(cartRepository.findById(any(Integer.class))).thenReturn(Mono.just(new Cart()));
        when(itemsRepository.findById(any(Integer.class))).thenReturn(Mono.just(item));
        when(itemsRepository.save(item)).thenReturn(Mono.just(new Item()));
        when(cartItemRepository.findByCartId(any(Integer.class))).thenReturn(Flux.just(cartItem));
        when(cartItemRepository.findByCartIdAndItemId(any(Integer.class),any(Integer.class))).thenReturn(Mono.just(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(Mono.just(cartItem));
        when(userRepository.findByUsername("senja")).thenReturn(Mono.just(user));
        cartService.changeCart(1, "plus", "senja").block();
        verify(itemsRepository, times(2)).findById(1);
    }

}
