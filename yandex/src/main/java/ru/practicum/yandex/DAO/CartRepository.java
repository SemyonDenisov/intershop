package ru.practicum.yandex.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.model.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findById(Integer cartId);
}
