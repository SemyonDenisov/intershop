package ru.practicum.yandex.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.practicum.yandex.model.Order;

public interface OrderRepository extends R2dbcRepository<Order, Integer> {
}
