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
import ru.practicum.yandex.dao.*;
import ru.practicum.yandex.model.*;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.security.model.User;
import ru.practicum.yandex.service.cache.itemCacheService.ItemCacheService;
import ru.practicum.yandex.service.orderService.OrderService;
import ru.practicum.yandex.service.paymentService.PaymentService;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceUnitTests {
    @Autowired
    private OrderService orderService;
    @MockitoBean
    private OrderRepository orderRepository;
    @MockitoBean
    private OrderItemRepository orderItemRepository;
    @MockitoBean
    private CartRepository cartRepository;
    @MockitoBean
    private ItemsRepository itemsRepository;
    @MockitoBean
    private CartItemRepository cartItemRepository;
    @MockitoBean
    private ItemCacheService itemCacheService;
    @MockitoBean
    private PaymentService paymentService;
    @MockitoBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {

        reset(orderRepository, orderItemRepository, cartRepository,paymentService);
    }

    @Test
    @WithMockUser(username = "senja")
    void test_findById() {
        Order order = new Order();
        order.setId(1);
        Item item = new Item();
        User user = new User();
        user.setId(1);
        user.setUsername("senja");
        user.setCartId(1);
        when(orderRepository.findByIdAndUserId(any(),any())).thenReturn(Mono.just(order));
        when(orderItemRepository.findAllByOrderId(1)).thenReturn(Flux.just(new OrderItem(1, 1, 3)));
        when(itemsRepository.findById(1)).thenReturn(Mono.just(item));
        when(userRepository.findByUsername("senja")).thenReturn(Mono.just(user));
        orderService.findById(1,"senja").subscribe();
        verify(orderRepository, times(1)).findByIdAndUserId(1,1);
        verify(orderItemRepository, times(1)).findAllByOrderId(1);
        verify(itemsRepository, times(1)).findById(1);
    }

    @Test
    @WithMockUser(username = "senja")
    void test_findAll() {
        Order order = new Order();
        order.setId(1);
        Item item = new Item();
        User user = new User();
        user.setId(1);
        user.setUsername("senja");
        user.setCartId(1);
        when(orderRepository.findAll()).thenReturn(Flux.just(order));
        when(orderRepository.findById(1)).thenReturn(Mono.just(order));
        when(orderRepository.findByIdAndUserId(any(),any())).thenReturn(Mono.just(order));
        when(orderItemRepository.findAllByOrderId(1)).thenReturn(Flux.just(new OrderItem(1, 1, 3)));
        when(itemsRepository.findById(1)).thenReturn(Mono.just(item));
        when(userRepository.findByUsername("senja")).thenReturn(Mono.just(user));
        orderService.findAll("senja").subscribe();
        verify(orderRepository, times(1)).findAll();
        verify(orderItemRepository, times(1)).findAllByOrderId(1);
        verify(itemsRepository, times(1)).findById(1);
    }

    @Test
    @WithMockUser(username = "senja")
    void test_createOrder() {
        Order order = new Order();
        order.setId(1);
        order.setTotalSum(1.0);
        CartItem cartItem = new CartItem(1, 1);
        cartItem.setCount(1);
        User user = new User();
        user.setId(1);
        user.setUsername("senja");
        user.setCartId(1);
        Cart cart = new Cart();
        cart.setUserId(1);
        cart.setId(1);
        Set<Item> items = new HashSet<>();
        Item item = new Item();
        item.setId(1);
        items.add(item);
        cart.setItems(items);
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));
        when(cartItemRepository.findByCartId(any(Integer.class))).thenReturn(Flux.just(cartItem));
        when(itemsRepository.save(any(Item.class))).thenReturn(Mono.just(new Item()));
        when(cartRepository.findById(any(Integer.class))).thenReturn(Mono.just(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(new Cart()));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(Mono.just(new OrderItem()));
        when(cartItemRepository.deleteByItemId(1)).thenReturn(Mono.empty());
        when(paymentService.makeOrder("senja",order)).thenReturn(Mono.empty());
        when(userRepository.findByUsername("senja")).thenReturn(Mono.just(user));

        when(itemsRepository.findById(1)).thenReturn(Mono.just(item));
        orderService.createOrder("senja").block();
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        verify(itemsRepository, times(1)).save(any(Item.class));
    }

}
