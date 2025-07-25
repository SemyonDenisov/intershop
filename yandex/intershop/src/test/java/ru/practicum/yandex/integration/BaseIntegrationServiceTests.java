package ru.practicum.yandex.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.dao.*;
import ru.practicum.yandex.EmbeddedRedisConfiguration;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;
import ru.practicum.yandex.service.orderService.OrderService;
import ru.practicum.yandex.service.paymentService.PaymentService;

import static org.mockito.Mockito.reset;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(EmbeddedRedisConfiguration.class)
@DirtiesContext
public class BaseIntegrationServiceTests {
    @Autowired
    protected ItemsRepository itemsRepository;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OrderItemRepository orderItemRepository;
    @Autowired
    protected CartItemRepository cartItemRepository;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected CartService cartService;
    @Autowired
    protected ItemService itemService;
    @Autowired
    protected UserRepository userRepository;

    @MockitoBean
    protected PaymentService paymentService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Transactional
    @BeforeEach
    public void baseSetUp() {
        cartRepository.deleteAll().block();
        orderRepository.deleteAll().block();
        orderItemRepository.deleteAll().block();
        itemsRepository.deleteAll().block();
        cartItemRepository.deleteAll().block();
        reset(paymentService);
    }
}
