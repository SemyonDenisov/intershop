package ru.practicum.yandex.service.cartService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DAO.CartItemRepository;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;

import java.util.HashSet;

@Service
public class CartServiceH2Impl implements CartService {

    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceH2Impl(CartRepository cartRepository,
                             ItemsRepository itemsRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.itemsRepository = itemsRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Mono<Cart> getCartById(Integer cartId) {
        return cartRepository.findById(cartId)
                .flatMap(cart-> cartItemRepository.findByCartId(cartId)
                .map(CartItem::getItemId)
                .flatMap(itemsRepository::findById)
                .collectList()
                .map(items->{
                    cart.setItems(new HashSet<>(items));
                    return cart;
                }));
    }

    @Override
    public Mono<Void> changeCart(Integer itemId, String action) {
        return cartItemRepository.findByCartIdAndItemId(1,itemId)
                .switchIfEmpty(cartItemRepository.save(new CartItem(1,itemId)))
                .zipWith(itemsRepository.findById(itemId))
                .flatMap(tuple->{
                    Item item = tuple.getT2();
                    switch (action.toUpperCase()) {
                        case "PLUS" -> {
                            item.setCount(item.getCount() + 1);
                            return itemsRepository.save(item);
                        }
                        case "MINUS" -> {
                            if(item.getCount()>0) {
                            item.setCount(item.getCount() - 1);
                            return itemsRepository.save(item);
                            }
                        }
                        case "DELETE" -> {
                            item.setCount(0);
                            return itemsRepository.save(item).zipWith(cartItemRepository.deleteByItemId(item.getId()));
                        }
                    }
                    return Mono.empty();
                }).then();
//        Mono.zip(
//                        this.getCartById(1),
//                        itemsRepository.findById(itemId)
//                ).onErrorResume(error -> Mono.zip(cartRepository.save(new Cart()), itemsRepository.findById(itemId)))
//                .flatMap(tuple -> {
//                    Cart cart = tuple.getT1();
//                    Item item = tuple.getT2();
//                    switch (action) {
//                        case "PLUS" -> item.setCount(item.getCount() + 1);
//                        case "MINUS" -> item.setCount(item.getCount() - 1);
//                        case "DELETE" -> {
//                            cart.getItems().remove(item);
//                            item.setCount(0);
//                        }
//                    }
//                    return Mono.zip(
//                            cartRepository.save(cart),
//                            itemsRepository.save(item)
//                    ).flatMap(tuple1 -> {
//                                tuple1.getT1();
//                                tuple1.getT2();
//                                return Mono.just(true);
//                            }
//                    );
//                });
    }
}
