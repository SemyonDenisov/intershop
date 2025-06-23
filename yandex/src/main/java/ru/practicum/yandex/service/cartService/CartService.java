package ru.practicum.yandex.service.cartService;

import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;

import java.util.List;
import java.util.Optional;

public interface CartService {
    Optional<Cart> getCartById(Integer cartId);
    void changeCart(Integer itemId,String action);
}
