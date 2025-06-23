package ru.practicum.yandex.service.cartService;

import ru.practicum.yandex.model.Cart;

import java.util.Optional;

public interface CartService {
    Optional<Cart> getCartById(Integer cartId);
}
