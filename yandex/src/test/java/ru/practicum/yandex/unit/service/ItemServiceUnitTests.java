package ru.practicum.yandex.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
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
        when(itemsRepository.save(any(Item.class))).thenReturn(new Item());
        MultipartFile emptyFile = new MockMultipartFile("file.png", new byte[0]);
        itemService.addItem("title", "text", 9.0, emptyFile);
        verify(itemsRepository, times(1)).save(any(Item.class));
    }


    @Test
    public void test_findById() {
        when(itemsRepository.findById(1)).thenReturn(Optional.of(new Item()));
        itemService.findById(1);
        verify(itemsRepository, times(1)).findById(1);
    }

    @Test
    public void test_findAll() {
        when(itemsRepository.findAllByTitleContainingIgnoreCase(PageRequest.of(1, 2), "1")).thenReturn(null);
        itemService.findAll(PageRequest.of(1, 2), "1");
        verify(itemsRepository, times(1)).findAllByTitleContainingIgnoreCase(PageRequest.of(1, 2), "1");
    }
}
