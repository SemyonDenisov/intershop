package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DAO.*;
import ru.practicum.yandex.model.*;
import ru.practicum.yandex.service.orderService.OrderService;

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

    @BeforeEach
    public void setUp() {
        reset(orderRepository, orderItemRepository, cartRepository);
    }

    @Test
    void test_findById() {
        Order order = new Order();
        order.setId(1);
        Item item = new Item();
        when(orderRepository.findById(1)).thenReturn(Mono.just(order));
        when(orderItemRepository.findAllByOrderId(1)).thenReturn(Flux.just(new OrderItem(1, 1, 3)));
        when(itemsRepository.findById(1)).thenReturn(Mono.just(item));
        orderService.findById(1).subscribe();
        verify(orderRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).findAllByOrderId(1);
        verify(itemsRepository, times(1)).findById(1);
    }

    @Test
    void test_findAll() {
        Order order = new Order();
        order.setId(1);
        Item item = new Item();
        when(orderRepository.findAll()).thenReturn(Flux.just(order));
        when(orderRepository.findById(1)).thenReturn(Mono.just(order));
        when(orderItemRepository.findAllByOrderId(1)).thenReturn(Flux.just(new OrderItem(1, 1, 3)));
        when(itemsRepository.findById(1)).thenReturn(Mono.just(item));
        orderService.findAll().subscribe();
        verify(orderRepository, times(1)).findAll();
        verify(orderItemRepository, times(1)).findAllByOrderId(1);
        verify(itemsRepository, times(1)).findById(1);
    }

    @Test
    void test_createOrder() {
        Order order = new Order();
        order.setId(1);
        order.setTotalSum(1.0);
        CartItem cartItem = new CartItem(1, 1);
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));
        when(cartItemRepository.findByCartId(any(Integer.class))).thenReturn(Flux.just(cartItem));
        when(itemsRepository.save(any(Item.class))).thenReturn(Mono.just(new Item()));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(new Cart()));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(Mono.just(new OrderItem()));
        when(cartItemRepository.deleteByItemId(1)).thenReturn(Mono.empty());
        Cart cart = new Cart();
        Set<Item> items = new HashSet<>();
        Item item = new Item();
        item.setId(1);
        items.add(item);
        cart.setItems(items);
        when(itemsRepository.findById(1)).thenReturn(Mono.just(item));
        orderService.createOrder(cart).block();
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        verify(itemsRepository, times(1)).save(any(Item.class));
    }

}
