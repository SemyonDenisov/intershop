package ru.practicum.yandex.service.cache.itemCacheService;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import ru.practicum.yandex.model.Item;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
public class ItemCacheServiceImpl implements ItemCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public ItemCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final Duration TTL = Duration.ofMinutes(1);

    @Override
    public void cacheItem(Item item, boolean deleteAll) {
        String key = "item:" + item.getId();
        redisTemplate.opsForValue().set(key, item, TTL);
        if (deleteAll) {
            deleteLists();
        }
    }

    @Override
    public Item getItem(int id) {
        Object item = redisTemplate.opsForValue().get("item:" + id);
        return item instanceof Item ? (Item) item : null;
    }

    @Override
    public List<Item> getAllItems(int skip, int limit, String sort, String search) {
        List<Item> cachedItems = (List<Item>) redisTemplate.opsForValue()
                .get("items:" + sort.toLowerCase() + ":"
                        + search.toLowerCase() + ":"
                        + skip +
                        ":" + limit + ":list");
        if (cachedItems == null || cachedItems.isEmpty()) {
            return List.of();
        }
        List<Item> range = cachedItems.stream().skip(skip).limit(limit).toList();
        if (range.isEmpty()) {
            return List.of();
        }
        return range.stream()
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void cacheItems(List<Item> items, String sort, String search, int skip, int limit) {
        redisTemplate.opsForValue().set("items:" + sort.toLowerCase() + ":"
                + search.toLowerCase() + ":"
                + skip +
                ":" + limit + ":list", items,TTL);
    }

    @Override
    public void deleteLists() {
        RedisConnection connection = redisTemplate.getRequiredConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions()
                .match("*items:*")
                .build();
        try (Cursor<byte[]> cursor = connection.scan(options)) {
            Set<String> keysToDelete = new HashSet<>();
            cursor.forEachRemaining(keyBytes -> {
                String key = new String(keyBytes, StandardCharsets.UTF_8);
                keysToDelete.add(key);
            });
            if (!keysToDelete.isEmpty()) {
                redisTemplate.delete(keysToDelete);
            }
        }
    }
}
