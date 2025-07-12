package ru.practicum.yandex.service.cartService;

import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Cart;


public interface CartService {
    Mono<Cart> getCartById(Integer cartId);
    Mono<Void> changeCart(Integer itemId,String action);
    Mono<Cart> getCart();
}
