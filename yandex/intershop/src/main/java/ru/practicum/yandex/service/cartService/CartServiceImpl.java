package ru.practicum.yandex.service.cartService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.dao.CartItemRepository;
import ru.practicum.yandex.dao.CartRepository;
import ru.practicum.yandex.dao.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.service.cache.itemCacheService.ItemCacheService;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ItemCacheService itemCacheService;

    @Override
    public Mono<Cart> getCartById(Integer cartId) {
        return cartRepository.findById(cartId)
                .switchIfEmpty(this.getCart(""))
                .flatMap(cart -> cartItemRepository.findByCartId(cartId)
                        .map(CartItem::getItemId)
                        .flatMap(itemsRepository::findById)
                        .collectList()
                        .map(items -> {
                            cart.setItems(new HashSet<>(items));
                            return cart;
                        }));
    }

    @Override
    public Mono<Void> changeCart(Integer itemId, String action, String username) {
        return getCartByUsername(username).map(Cart::getId)
                .flatMap(cartId -> cartItemRepository
                        .findByCartIdAndItemId(cartId, itemId)
                        .switchIfEmpty(cartItemRepository.save(new CartItem(cartId, itemId)))
                )
                .zipWith(itemsRepository.findById(itemId))
                .flatMap(tuple -> {
                    CartItem cartItem = tuple.getT1();
                    Item item = tuple.getT2();
                    switch (action.toUpperCase()) {
                        case "PLUS" -> {
                            cartItem.setCount(cartItem.getCount() + 1);
                            item.setCount(item.getCount() + 1);
                            itemCacheService.cacheItem(item, true);
                            return itemsRepository.save(item).zipWith(cartItemRepository.save(cartItem));
                        }
                        case "MINUS" -> {
                            if (item.getCount() > 0) {
                                item.setCount(item.getCount() - 1);
                                cartItem.setCount(cartItem.getCount() - 1);
                                itemCacheService.cacheItem(item, true);
                                return itemsRepository.save(item).zipWith(cartItemRepository.save(cartItem));
                            }
                        }
                        case "DELETE" -> {
                            item.setCount(0);
                            itemCacheService.cacheItem(item, true);
                            return itemsRepository.save(item).zipWith(cartItemRepository.deleteByItemId(item.getId()));
                        }
                    }
                    return Mono.empty();
                }).then();
    }

    @Override
    public Mono<Cart> getCart(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    Cart cart1 = new Cart();
                    cart1.setInfo(username + " cart");
                    cart1.setUserId(user.getId());
                    return cartRepository.save(cart1);
                });
    }


    @Override
    public Mono<Cart> getCartByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .flatMap(user -> cartRepository.findById(user.getCartId()))
                .switchIfEmpty(this.getCart(username))
                .flatMap(cart -> cartItemRepository
                        .findByCartId(cart.getId())
                        .flatMap(cartItem -> itemsRepository.findById(cartItem.getItemId()))
                        .collectList()
                        .map(items -> {
                            cart.setItems(new HashSet<>(items));
                            return cart;
                        }));
    }
}
