package ru.practicum.yandex.integration.service;


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
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.integration.BaseIntegrationTests;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.itemService.ItemService;


import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceIntegrationTests extends BaseIntegrationTests {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemsRepository itemsRepository;

    @BeforeEach
    public void setUp() {
        itemsRepository.deleteAll();
        itemsRepository.save(new Item("title1", "description1", 1.0, 0, "1.jpg"));
        itemsRepository.save(new Item("title2", "description2", 2.0, 1, "1.jpg"));
        itemsRepository.save(new Item("title3", "description3", 3.0, 2, "1.jpg"));
        itemsRepository.save(new Item("title4", "description4", 4.0, 3, "1.jpg"));
        itemsRepository.save(new Item("title5", "description5", 5.0, 4, "1.jpg"));
    }

    @Nested
    class AddItemTest {

        @Value("${spring.image.savePath}")
        String imageStorePath;

        void cleanup() {
            List<Item> items = itemsRepository.findAll();
            String imagePath = items.get(items.size() - 1).getImgPath();
            new File(imageStorePath+imagePath).delete();
        }

        @Test
        public void test_addItem() {
            try {
                MultipartFile emptyFile = new MockMultipartFile("file.png", new byte[0]);
                itemService.addItem("title", "text", 9.0, emptyFile);
                List<Item> items = itemsRepository.findAll();
                Item item = itemsRepository.findById(items.get(items.size() - 1).getId()).get();
                assertEquals("title", item.getTitle());
                cleanup();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Test
    public void test_findById() {
        int id = itemsRepository.findAll().get(0).getId();
        Item item = itemService.findById(id).get();
        assertEquals(item.getId(), id);
        assertEquals("title1", item.getTitle());
    }

    @Test
    public void test_findAll() {
        Page<Item> items = itemService.findAll(PageRequest.of(0, 2), "t");
        assertEquals(2, items.getContent().size());
        assertEquals(5, items.getTotalElements());
    }

}
