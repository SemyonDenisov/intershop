package ru.practicum.yandex.service.itemService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.yandex.model.Item;


import java.util.Optional;

public interface ItemService {
    Page<Item> findAll(PageRequest pageable);
    Optional<Item> findById(Integer id);
    void addItem(String title, String description, Double price, MultipartFile image);
}
