package ru.practicum.yandex.service.orderService;

import ru.practicum.yandex.DAO.OrderRepository;
import ru.practicum.yandex.model.Order;

import java.util.Optional;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }
}
