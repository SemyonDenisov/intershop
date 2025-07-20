package ru.practicum.yandex.service.cache.itemCacheService;

import ru.practicum.yandex.model.Item;

import java.util.List;

public interface ItemCacheService {
    void cacheItem(Item item, boolean deleteAll);
    Item getItem(int id);
    List<Item> getAllItems(int skip, int limit, String sort,String search);
    void cacheItems(List<Item> items, String sort, String search, int skip, int limit);
    void deleteLists();
}
