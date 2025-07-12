package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.itemService.ItemService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceUnitTests {
    @MockitoBean
    private ItemsRepository itemsRepository;

    @Autowired
    private ItemService itemService;

    @BeforeEach
    void resetMocks() {
        reset(itemsRepository);
    }


    @Test
    public void test_addItem() {
        when(itemsRepository.save(any(Item.class))).thenReturn(Mono.just(new Item()));
        FilePart mockFilePart = mock(FilePart.class);
        when(mockFilePart.transferTo(any(File.class))).thenReturn(Mono.empty());
        when(mockFilePart.filename()).thenReturn("file.png");
        itemService.addItem("title", "text", 9.0, Mono.just(mockFilePart)).block();
        verify(itemsRepository, times(1)).save(any(Item.class));
    }


    @Test
    public void test_findById() {
        when(itemsRepository.findById(1)).thenReturn(Mono.just(new Item()));
        itemService.findById(1).block();
        verify(itemsRepository, times(1)).findById(1);
    }

    @Test
    public void test_findAll() {
        Sort sort =Sort.by(Sort.Direction.ASC, "title");
        when(itemsRepository.findAllByTitleContainingIgnoreCase("1", sort)).thenReturn(Flux.just(new Item()));
        itemService.findAll( 0,1,"1",sort).collectList().block();
        verify(itemsRepository, times(1)).findAllByTitleContainingIgnoreCase("1", sort);
    }
}
