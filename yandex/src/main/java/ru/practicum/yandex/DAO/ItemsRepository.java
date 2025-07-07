package ru.practicum.yandex.DAO;


import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Item;



@Repository
public interface ItemsRepository extends R2dbcRepository<Item, Integer> {
    @NonNull
    Flux<Item> findAllByTitleContainingIgnoreCase(@NonNull Pageable pageable, String title);
    @NonNull
    Mono<Item> findById(@NonNull Integer id);

    @NonNull
    Flux<Item> findAll(Pageable pageable);
}
