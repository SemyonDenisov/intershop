package ru.practicum.yandex.DAO;

import lombok.NonNull;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.CartItem;


public interface CartItemRepository extends R2dbcRepository<CartItem, Integer> {
    @NonNull
    Flux<CartItem> findByCartId(Integer cartId);

    Mono<CartItem> findByCartIdAndItemId(Integer cartId, Integer itemId);

    Mono<Void> deleteByItemId(Integer itemId);
}
