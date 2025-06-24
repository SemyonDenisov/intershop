package ru.practicum.yandex.service.itemService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Item;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemServiceH2Impl implements ItemService {

    @Value("classpath:/static/")
    private String imageSavePath;

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

    @Override
    public void addItem(String title, String description, Double price, MultipartFile image) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setDescription(description);
        if (image != null) {
            try {
                UUID uuid = UUID.randomUUID();
                String path = System.getProperty("java.class.path").split(";")[0]+"\\static\\images\\";
                String extension = Objects.requireNonNull(image.getOriginalFilename()).split("\\.")[1];
                String name = uuid + "." + extension;
                image.transferTo(new File(path+name));
                item.setImgPath(name);
                itemsRepository.save(item);
            } catch (Exception ignored) {
            }
        }
    }
}
