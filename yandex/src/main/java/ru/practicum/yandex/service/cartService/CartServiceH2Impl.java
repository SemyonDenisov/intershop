package ru.practicum.yandex.service.cartService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;

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
    public Mono<Cart> getCartById(Integer cartId) {
        return cartRepository.findById(cartId);
    }

    @Override
    public Mono<Boolean> changeCart(Integer itemId, String action) {
        return Mono.zip(
                cartRepository.findById(1),
                itemsRepository.findById(itemId)
        ).flatMap(tuple -> {
            Cart cart = tuple.getT1();
            Item item = tuple.getT2();
            switch (action) {
                case "PLUS" -> item.setCount(item.getCount() + 1);
                case "MINUS" -> item.setCount(item.getCount() - 1);
                case "DELETE" -> {
                    cart.getItems().remove(item);
                    item.setCount(0);
                }
            }
            return Mono.zip(
                    cartRepository.save(cart),
                    itemsRepository.save(item)
            ).flatMap(tuple1 -> {
                        tuple1.getT1();
                        tuple1.getT2();
                        return Mono.just(true);
                    }
            );
        });
    }
}
