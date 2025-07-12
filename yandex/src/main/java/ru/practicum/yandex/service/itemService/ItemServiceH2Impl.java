package ru.practicum.yandex.service.itemService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Item;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Objects;

import java.util.UUID;

@Service
public class ItemServiceH2Impl implements ItemService {

    @Value("${spring.image.savePath}")
    String imagePath;

    private final ItemsRepository itemsRepository;

    ItemServiceH2Impl(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public Flux<Item> findAll(int pageSize,int pageNumber, String title, Sort sort) {
        if (title == null || title.isEmpty()) {
            return itemsRepository.findAll(sort).skip((long) pageSize * (pageNumber - 1))
                    .take(pageSize);
        } else return itemsRepository.findAllByTitleContainingIgnoreCase(title,sort).skip((long) pageSize * (pageNumber - 1))
                .take(pageSize);
    }

    @Override
    public Mono<Item> findById(Integer id) {
        return itemsRepository.findById(id);
    }

    @Override
    public Mono<Item> addItem(String title, String description, Double price, Mono<FilePart> imageMono) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setDescription(description);
        return imageMono.flatMap(image -> {
            UUID uuid = UUID.randomUUID();
            String originalFilename = Objects.requireNonNull(image.filename());
            String extension = originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1)
                    : "bin";
            String name = uuid + "." + extension;
            item.setImgPath(name);
            Mono<Void> savedImageMono = image.transferTo(new File(imagePath + name));
            return savedImageMono.then(itemsRepository.save(item));
        }).doOnError(throwable -> {
            System.out.println("file not downloaded");
            throw new RuntimeException(throwable);
        });
    }

    @Override
    public byte[] getImage(String filename) throws IOException {
        return Files.readAllBytes(Path.of(imagePath + filename));
    }

    @Override
    public Mono<Long> getCount(){
        return itemsRepository.count();
    }
}
