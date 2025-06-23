package ru.practicum.yandex.service.cartService;

import org.springframework.stereotype.Service;
import ru.practicum.yandex.DAO.CartItemRepository;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemsRepository itemsRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ItemsRepository itemsRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemsRepository = itemsRepository;
    }

    @Override
    public Optional<Cart> getCartById(Integer cartId) {
        return cartRepository.findById(cartId);
    }


    @Override
    public void addToCart(Integer itemId, String action) {
        Cart cart = cartRepository.findById(1).orElse(null);
        if(cart==null) {
            cart = new Cart();
        }

        Item item = itemsRepository.findById(itemId).orElse(null);
        if (item != null) {
            CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId())
                    .orElse(new CartItem(cart.getId(), itemId, 0));
            switch (action.toUpperCase()) {
                case "PLUS" -> {
                    if (item.getCount() > 1) {
                        item.setCount(item.getCount() - 1);
                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                        cartItemRepository.save(cartItem);
                        itemsRepository.save(item);
                    }
                }
                case "MINUS" -> {
                    if (cartItem.getQuantity() > 1) {
                        item.setCount(item.getCount() + 1);
                        cartItem.setQuantity(cartItem.getQuantity() - 1);
                        cartItemRepository.save(cartItem);
                        itemsRepository.save(item);
                    }
                }
            }
        }

    }
}
