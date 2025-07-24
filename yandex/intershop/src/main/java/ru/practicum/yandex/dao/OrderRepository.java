package ru.practicum.yandex.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Order;

public interface OrderRepository extends R2dbcRepository<Order, Integer> {
    Mono<Order> findByIdAndUserId(Integer orderId, Integer userId);
}
