package ru.practicum.yandex.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.model.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findAllByOrderId(int orderId);
}
