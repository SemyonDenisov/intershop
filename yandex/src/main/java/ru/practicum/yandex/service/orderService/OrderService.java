package ru.practicum.yandex.service.orderService;

import ru.practicum.yandex.model.Order;

import java.util.Optional;

public interface OrderService {
    Optional<Order> findById(Integer id);
}
