package ru.practicum.yandex.DAO;


import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.yandex.model.Item;

import java.util.Optional;


@Repository
public interface ItemsRepository extends JpaRepository<Item, Integer> {
    @NonNull
    Page<Item> findAllByTitleContainingIgnoreCase(@NonNull Pageable pageable,String title);
    @NonNull
    Optional<Item> findById(@NonNull Integer id);
}
