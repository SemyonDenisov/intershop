package ru.practicum.yandex.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.practicum.yandex.model.OrderItem;


public interface OrderItemRepository extends R2dbcRepository<OrderItem, Integer> {
    Flux<OrderItem> findAllByOrderId(int orderId);
}
