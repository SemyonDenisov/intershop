package ru.practicum.yandex.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.DAO.OrderItemRepository;
import ru.practicum.yandex.DAO.OrderRepository;

@SpringBootTest
@ActiveProfiles("test")
public class BaseIntegrationTests {
    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    @Transactional
    @BeforeEach
    public void baseSetUp() {
        cartRepository.deleteAll().block();
        orderRepository.deleteAll().block();
        orderItemRepository.deleteAll().block();
        itemsRepository.deleteAll().block();
    }
}
