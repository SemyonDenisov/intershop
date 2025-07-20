package ru.practicum.yandex.service.cartService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DAO.CartItemRepository;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.CartItem;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cache.itemCacheService.ItemCacheService;

import java.util.HashSet;
import java.util.List;

@Service
public class CartServiceH2Impl implements CartService {

    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;
    private final CartItemRepository cartItemRepository;

    private final ItemCacheService itemCacheService;

    public CartServiceH2Impl(CartRepository cartRepository,
                             ItemsRepository itemsRepository,
                             CartItemRepository cartItemRepository,
                             ItemCacheService itemCacheService) {
        this.cartRepository = cartRepository;
        this.itemsRepository = itemsRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemCacheService = itemCacheService;
    }

    @Override
    public Mono<Cart> getCartById(Integer cartId) {
        return cartRepository.findById(cartId)
                .switchIfEmpty(this.getCart())
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
    public Mono<Void> changeCart(Integer itemId, String action) {
        return cartItemRepository.findByCartIdAndItemId(1, itemId)
                .switchIfEmpty(cartItemRepository.save(new CartItem(1, itemId)))
                .zipWith(itemsRepository.findById(itemId))
                .flatMap(tuple -> {
                    Item item = tuple.getT2();
                    switch (action.toUpperCase()) {
                        case "PLUS" -> {
                            item.setCount(item.getCount() + 1);
                            itemCacheService.cacheItem(item, true);
                            return itemsRepository.save(item);
                        }
                        case "MINUS" -> {
                            if (item.getCount() > 0) {
                                item.setCount(item.getCount() - 1);
                                itemCacheService.cacheItem(item, true);
                                return itemsRepository.save(item);
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
    public Mono<Cart> getCart() {
        return cartRepository.findAll().collectList().map(List::getFirst);
    }
}
