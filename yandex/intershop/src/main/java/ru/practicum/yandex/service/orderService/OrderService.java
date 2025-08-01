package ru.practicum.yandex.service.orderService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.dto.OrderWithItems;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Order;


public interface OrderService {
    Flux<OrderWithItems> findAll(String username);

    Mono<OrderWithItems> findById(int id, String username);

    Mono<Order> createOrder(String username);
}
