package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.DAO.OrderItemRepository;
import ru.practicum.yandex.DAO.OrderRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.model.OrderItem;
import ru.practicum.yandex.service.orderService.OrderService;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
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

    @BeforeEach
    public void setUp() {
        reset(orderRepository, orderItemRepository, cartRepository);
    }

    @Test
    void test_findById(){
        Order order = new Order();
        order.setId(1);
        Item item = new Item();
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemRepository.findAllByOrderId(1)).thenReturn(List.of(new OrderItem(1,1,3)));
        when(itemsRepository.findById(1)).thenReturn(Optional.of(item));
        orderService.findById(1);
        verify(orderRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).findAllByOrderId(1);
        verify(itemsRepository, times(1)).findById(1);
    }

    @Test
    void test_findAll(){
        Order order = new Order();
        order.setId(1);
        Item item = new Item();
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderItemRepository.findAllByOrderId(1)).thenReturn(List.of(new OrderItem(1,1,3)));
        when(itemsRepository.findById(1)).thenReturn(Optional.of(item));
        orderService.findAll();
        verify(orderRepository, times(1)).findAll();
        verify(orderItemRepository, times(1)).findAllByOrderId(1);
        verify(itemsRepository, times(1)).findById(1);
    }

    @Test
    void test_createOrder(){
        Order order = new Order();
        order.setId(1);
        order.setTotalSum(1.0);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());
        when(itemsRepository.save(any(Item.class))).thenReturn(new Item());
        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart());
        Cart cart = new Cart();
        Set<Item> items = new HashSet<>();
        Item item = new Item();
        item.setId(1);
        items.add(item);
        cart.setItems(items);
        orderService.createOrder(cart);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        verify(itemsRepository, times(1)).save(any(Item.class));
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

}
