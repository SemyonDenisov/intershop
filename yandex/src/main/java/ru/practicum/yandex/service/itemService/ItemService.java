package ru.practicum.yandex.service.itemService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.yandex.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Page<Item> findAll(PageRequest pageable);
    Optional<Item> findById(Integer id);
}
