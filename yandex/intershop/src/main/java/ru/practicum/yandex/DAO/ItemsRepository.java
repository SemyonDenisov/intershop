package ru.practicum.yandex.DAO;


import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Item;



@Repository
public interface ItemsRepository extends R2dbcRepository<Item, Integer> {
    @NonNull
    Flux<Item> findAllByTitleContainingIgnoreCase(String title,Sort sort);
    @NonNull
    Mono<Item> findById(@NonNull Integer id);

    @NonNull
    Flux<Item> findAll(@NonNull Sort sort);
}
