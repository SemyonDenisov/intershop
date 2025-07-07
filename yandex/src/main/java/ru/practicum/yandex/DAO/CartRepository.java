package ru.practicum.yandex.DAO;

import lombok.NonNull;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Cart;


@Repository
public interface CartRepository extends R2dbcRepository<Cart, Integer> {
    @NonNull
    Mono<Cart> findById(@NonNull Integer cartId);
}
