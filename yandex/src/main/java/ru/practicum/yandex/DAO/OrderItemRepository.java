package ru.practicum.yandex.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    Optional<OrderItem> findByOrderId(int orderId);
    List<OrderItem> findAllByOrderId(int orderId);
    Optional<OrderItem> findByOrderIdAndItemId(int orderId, int itemId);
    Optional<OrderItem> findByItemId(int itemId);
}
