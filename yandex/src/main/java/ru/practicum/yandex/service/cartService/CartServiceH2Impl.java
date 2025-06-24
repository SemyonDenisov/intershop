package ru.practicum.yandex.service.cartService;

import org.springframework.stereotype.Service;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;

import java.util.Optional;

@Service
public class CartServiceH2Impl implements CartService {

    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;

    public CartServiceH2Impl(CartRepository cartRepository,
                             ItemsRepository itemsRepository) {
        this.cartRepository = cartRepository;
        this.itemsRepository = itemsRepository;
    }

    @Override
    public Optional<Cart> getCartById(Integer cartId) {
        return cartRepository.findById(cartId);
    }


    @Override
    public void changeCart(Integer itemId, String action) {
        Cart cart = cartRepository.findById(1).orElse(null);
        if (cart == null) {
            cart = new Cart();
        }

        Item item = itemsRepository.findById(itemId).orElse(null);
        if (item != null) {
            switch (action.toUpperCase()) {
                case "PLUS" -> {
                    item.setCount(item.getCount() + 1);
                    cart.getItems().add(item);
                    cartRepository.save(cart);
                    itemsRepository.save(item);

                }
                case "MINUS" -> {
                    if (item.getCount() > 1) {
                        item.setCount(item.getCount() - 1);
                        if (item.getCount() == 0) {
                            cart.getItems().remove(item);
                        }
                        cartRepository.save(cart);
                        itemsRepository.save(item);
                    }
                }
                case "DELETE"->{
                    cart.getItems().remove(item);
                    item.setCount(0);
                    cartRepository.save(cart);
                    itemsRepository.save(item);
                }
            }
        }

    }
}
