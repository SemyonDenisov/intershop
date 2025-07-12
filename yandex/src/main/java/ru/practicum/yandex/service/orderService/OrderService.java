package ru.practicum.yandex.service.orderService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Order;


public interface OrderService {
    Flux<OrderWithItems> findAll();

    Mono<OrderWithItems> findById(int id);

    Mono<Order> createOrder(Cart cart);
}
