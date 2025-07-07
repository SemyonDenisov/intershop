package ru.practicum.yandex.service.cartService;

import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Cart;

import java.util.Optional;

public interface CartService {
    Mono<Cart> getCartById(Integer cartId);
    Mono<Boolean> changeCart(Integer itemId,String action);
}
