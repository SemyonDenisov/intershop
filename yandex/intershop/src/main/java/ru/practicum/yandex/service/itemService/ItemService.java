package ru.practicum.yandex.service.itemService;

import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Item;


import java.io.IOException;

public interface ItemService {
    Flux<Item> findAll(int pageSize, int pageNumber, String title, Sort sort);

    Mono<Item> findById(Integer id);

    Mono<Item> addItem(String title, String description, Double price, Mono<FilePart> imageMono);

    byte[] getImage(String filename) throws IOException;

    Mono<Long> getCount();

}
