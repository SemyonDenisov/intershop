package ru.practicum.yandex.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
