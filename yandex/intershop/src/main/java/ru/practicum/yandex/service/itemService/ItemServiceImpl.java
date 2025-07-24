package ru.practicum.yandex.service.itemService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.dao.CartItemRepository;
import ru.practicum.yandex.dao.ItemsRepository;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cache.itemCacheService.ItemCacheService;
import ru.practicum.yandex.service.cartService.CartService;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;
import java.util.Objects;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Value("${spring.image.savePath}")
    String imagePath;

    private final ItemsRepository itemsRepository;

    private final ItemCacheService itemCacheService;

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;


    private Mono<Item> getItemsWithActualCount(Item item, Integer cartId) {
        if (cartId <= 0) {
            return Mono.just(item);
        } else {
            return cartItemRepository.findByCartIdAndItemId(cartId, item.getId())
                    .map(cartItem -> {
                        if (cartItem == null) {
                            item.setCount(0);
                        } else {
                            item.setCount(cartItem.getCount());
                        }
                        return item;
                    })
                    .switchIfEmpty(Mono.just(item));
        }
    }

    @Override
    public Flux<Item> findAll(int pageSize, int pageNumber, String title, String sort, String username) {
//        List<Item> cachedItems = itemCacheService.getAllItems((pageNumber - 1) * pageSize, pageSize, sort, title);
//        if (cachedItems != null && !cachedItems.isEmpty()) {
//            return Flux.fromIterable(cachedItems);
//        }

        Sort sortForRequest;
        switch (sort) {
            case "AlPHA" -> sortForRequest = Sort.by(Sort.Direction.ASC, "title");
            case "PRICE" -> sortForRequest = Sort.by(Sort.Direction.ASC, "price");
            default -> sortForRequest = Sort.by(Sort.Direction.DESC, "id");
        }
        Mono<Integer> cartIdMono;
        if (!username.isEmpty()) {
            cartIdMono = cartService.getCartByUsername(username).map(Cart::getId);
        } else {
            cartIdMono = Mono.just(-1);
        }
        if (title == null || title.isEmpty()) {
            return cartIdMono.flatMapMany(cartId ->
                    itemsRepository.findAll(sortForRequest)
                            .flatMap(item -> this.getItemsWithActualCount(item, cartId))
                            .collectList()
                            .map(items ->
                                    {
                                        itemCacheService.cacheItems(items, sort, title, (pageNumber - 1) * pageSize, pageSize);
                                        return items;
                                    }
                            )
                            .flatMapMany(Flux::fromIterable)
                            .skip((long) pageSize * (pageNumber - 1))
                            .take(pageSize));
        } else {
            return cartIdMono.flatMapMany(cartId -> itemsRepository.findAllByTitleContainingIgnoreCase(title, sortForRequest)
                    .flatMap(item -> this.getItemsWithActualCount(item, cartId))
                    .collectList()
                    .map(items ->
                            {
                                itemCacheService.cacheItems(items, sort, title, (pageNumber - 1) * pageSize, pageSize);
                                return items;
                            }
                    )
                    .flatMapMany(Flux::fromIterable).skip((long) pageSize * (pageNumber - 1))
                    .take(pageSize));
        }
    }

    @Override
    public Mono<Item> findById(Integer id) {
        Item item = itemCacheService.getItem(id);
        Mono<Item> itemMono = Mono.justOrEmpty(item);
        if (item == null) {
            itemMono = itemsRepository
                    .findById(id)
                    .doOnNext(item1 -> itemCacheService.cacheItem(item1, false));
        }
        return itemMono;
    }

    @Override
    public Mono<Item> addItem(String title, String description, Double price, Mono<FilePart> imageMono) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setDescription(description);
        return imageMono.flatMap(image -> {
            UUID uuid = UUID.randomUUID();
            String originalFilename = Objects.requireNonNull(image.filename());
            String extension = originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1)
                    : "bin";
            String name = uuid + "." + extension;
            item.setImgPath(name);
            Mono<Void> savedImageMono = image.transferTo(new File(imagePath + name));
            itemCacheService.cacheItem(item, true);
            return savedImageMono
                    .then(itemsRepository.save(item));
        }).doOnError(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Override
    public byte[] getImage(String filename) throws IOException {
        return Files.readAllBytes(Path.of(imagePath + filename));
    }

    @Override
    public Mono<Long> getCount() {
        return itemsRepository.count();
    }
}
