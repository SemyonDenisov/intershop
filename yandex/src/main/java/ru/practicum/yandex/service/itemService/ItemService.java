package ru.practicum.yandex.service.itemService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.yandex.model.Item;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

public interface ItemService {
    Page<Item> findAll(PageRequest pageable,String title);
    Optional<Item> findById(Integer id);
    void addItem(String title, String description, Double price, MultipartFile image);
    byte[] getImage(String filename) throws IOException;
}
