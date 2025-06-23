package ru.practicum.yandex.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.model.CartItem;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartIdAndItemId(int cartId, int itemId);
}
