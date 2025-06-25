package ru.practicum.yandex.DAO;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.model.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @NonNull
    Optional<Cart> findById(@NonNull Integer cartId);
}
