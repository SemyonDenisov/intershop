package ru.practicum.yandex.service.orderService;

import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderWithItems> findAll();
    Optional<OrderWithItems> findById(int id);
    void save(Order order);
    void createOrder(Cart cart);
}
