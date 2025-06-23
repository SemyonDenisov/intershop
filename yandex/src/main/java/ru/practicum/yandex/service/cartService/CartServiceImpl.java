package ru.practicum.yandex.service.cartService;

import org.springframework.stereotype.Service;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.model.Cart;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Optional<Cart> getCartById(Integer cartId) {
        return cartRepository.findById(cartId);
    }
}
