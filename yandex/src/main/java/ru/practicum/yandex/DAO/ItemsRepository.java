package ru.practicum.yandex.DAO;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.yandex.model.Item;

import java.util.Optional;


@Repository
public interface ItemsRepository extends JpaRepository<Item, Integer> {
    Page<Item> findAll(Pageable pageable);
    Optional<Item> findById(Integer id);
}
