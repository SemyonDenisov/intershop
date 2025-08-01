package ru.practicum.yandex.integration.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.integration.BaseIntegrationServiceTests;
import ru.practicum.yandex.model.Item;


import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceIntegrationTests extends BaseIntegrationServiceTests {

    @BeforeEach
    public void setUp() {
        itemsRepository.save(new Item("title1", "description1", 1.0, 0, "1.jpg")).block();
        itemsRepository.save(new Item("title2", "description2", 2.0, 1, "1.jpg")).block();
        itemsRepository.save(new Item("title3", "description3", 3.0, 2, "1.jpg")).block();
        itemsRepository.save(new Item("title4", "description4", 4.0, 3, "1.jpg")).block();
        itemsRepository.save(new Item("title5", "description5", 5.0, 4, "1.jpg")).block();
    }


    @Test
    @WithMockUser(username = "senja",roles = "MODERATOR")
    public void test_addItem() {
        try {
            FilePart mockFilePart = mock(FilePart.class);
            when(mockFilePart.filename()).thenReturn("test.png");
            when(mockFilePart.transferTo(any(File.class))).thenReturn(Mono.empty());

            itemService.addItem("title", "text", 9.0, Mono.just(mockFilePart)).block();
            List<Item> items = itemsRepository.findAll().collectList().block();
            Item item = itemsRepository.findById(items.get(items.size() - 1).getId()).block();
            assertEquals("title", item.getTitle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @WithMockUser(username = "senja")
    public void test_findById() {
        int id = itemsRepository.findAll().collectList().block().get(0).getId();
        Item item = itemService.findById(id).block();
        assertEquals(item.getId(), id);
        assertEquals("title1", item.getTitle());
    }

    @Test
    @WithMockUser(username = "")
    public void test_findAll() {
        List<Item> items = itemService.findAll(3, 1, "t", "NO", "").collectList().block();
        assertEquals(3, items.size());
    }

}
