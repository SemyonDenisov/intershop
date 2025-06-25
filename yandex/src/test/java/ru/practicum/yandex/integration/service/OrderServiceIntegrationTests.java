package ru.practicum.yandex.integration.service;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.DAO.OrderItemRepository;
import ru.practicum.yandex.DAO.OrderRepository;
import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.integration.BaseIntegrationTests;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.service.orderService.OrderService;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceIntegrationTests extends BaseIntegrationTests {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemsRepository itemsRepository;
    @Autowired
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        itemsRepository.deleteAll();
        Item it1 = itemsRepository.save(new Item("title2","description2",2.0,1,""));
        Item it2 = itemsRepository.save(new Item("title3","description3",3.0,1,""));
        Cart cart = new Cart();
        cart.getItems().add(it1);
        cart.getItems().add(it2);
        cartRepository.save(cart);
        orderRepository.save(new Order(3.5));
        orderRepository.save(new Order(4.0));
    }

    @Test
    void test_findById(){
        OrderWithItems order = orderService.findById(3).get();
        assertEquals(3.5,order.getTotalSum());
    }

    @Test
    void test_findAll(){
        List<OrderWithItems> orders = orderService.findAll();
        assertEquals(2, orders.size());
    }

    @Test
    @Transactional
    void test_createOrder(){
        orderService.createOrder(cartRepository.findById(3).get());
        assertEquals(3,orderRepository.findAll().size());
    }

}
