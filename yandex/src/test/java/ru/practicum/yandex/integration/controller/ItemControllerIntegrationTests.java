package ru.practicum.yandex.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.integration.BaseIntegrationTests;
import ru.practicum.yandex.model.Item;


import java.io.File;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ItemControllerIntegrationTests extends BaseIntegrationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    CartRepository cartRepository;

    @BeforeEach
    public void setUp() {
        itemsRepository.deleteAll();
        cartRepository.deleteAll();
        itemsRepository.save(new Item("title1", "description1", 1.0, 0, ""));
        itemsRepository.save(new Item("title2", "description2", 2.0, 1, ""));
        itemsRepository.save(new Item("title3", "description3", 3.0, 2, ""));
        itemsRepository.save(new Item("title4", "description4", 4.0, 3, ""));
        itemsRepository.save(new Item("title5", "description5", 5.0, 4, ""));
    }

    @Test
    void test_getItemInfo() throws Exception {
        int id = itemsRepository.findAll().get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/items/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("item"))
                .andExpect(xpath("//body//div//p[2]//b[1]").string("title1"));
    }

    @Test
    void test_addItemToCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/items/8")
                        .queryParam("action", "plus"))
                .andExpect(status().is3xxRedirection())
                .andExpect(
                        header().stringValues(HttpHeaders.LOCATION, "/items/8")

                );
    }

    @Nested
    class CleanupTest {

        @Value("${spring.image.savePath}")
        String imageStorePath;

        void cleanup() {
            List<Item> items = itemsRepository.findAll();
            String imagePath = items.get(items.size() - 1).getImgPath();
            new File(imageStorePath + imagePath).delete();
        }

        @Test
        void test_addItem() throws Exception {
            try {
                MockMultipartFile file = new MockMultipartFile("image", "test.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test".getBytes());
                mockMvc.perform(MockMvcRequestBuilders.multipart("/items/add").file(file)
                                .part(new MockPart("title", "".getBytes()))
                                .file(file)
                                .part(new MockPart("description", "".getBytes()))
                                .part(new MockPart("price", "1.0".getBytes()))
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(header().stringValues(HttpHeaders.LOCATION, "/main/items"));
                cleanup();
            } catch (Exception e) {
                throw e;
            }
        }
    }
}
