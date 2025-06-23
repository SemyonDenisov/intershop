package ru.practicum.yandex.service.itemService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Item;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceH2Impl implements ItemService {
    private final ItemsRepository itemsRepository;

    ItemServiceH2Impl(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public Page<Item> findAll(PageRequest pageable) {
        return itemsRepository.findAll(pageable);
    }

    @Override
    public Optional<Item> findById(Integer id) {
        return itemsRepository.findById(id);
    }
}
