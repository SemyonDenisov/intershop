package ru.practicum.yandex.service.orderService;

import org.springframework.stereotype.Service;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.DAO.OrderItemRepository;
import ru.practicum.yandex.DAO.OrderRepository;
import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.model.OrderItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceH2Impl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceH2Impl(OrderRepository orderRepository,
                              CartRepository cartRepository,
                              ItemsRepository itemsRepository,
                              OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.itemsRepository = itemsRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderWithItems> findAll() {
        List<OrderWithItems> orderWithItems = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> {
            List<Item> foundItems = new ArrayList<>();
            orderItemRepository.findAllByOrderId(order.getId()).forEach(orderItem -> {
                itemsRepository.findById(orderItem.getItemId()).ifPresent(item -> {
                    item.setCount(orderItem.getQuantity());
                    foundItems.add(item);
                });
            });
            orderWithItems.add(new OrderWithItems(order.getId(), foundItems, order.getTotalSum()));
        });
        return orderWithItems;
    }

    @Override
    public Optional<OrderWithItems> findById(int id) {
        OrderWithItems orderWithItems = new OrderWithItems();
        orderRepository.findById(id).ifPresent(order -> {
            List<Item> foundItems = new ArrayList<>();
            orderItemRepository.findAllByOrderId(order.getId()).forEach(orderItem -> {
                itemsRepository.findById(orderItem.getItemId()).ifPresent(item -> {
                    item.setCount(orderItem.getQuantity());
                    foundItems.add(item);
                });
            });
            orderWithItems.setId(order.getId());
            orderWithItems.setTotalSum(order.getTotalSum());
            orderWithItems.setItems(foundItems);
        });
        return Optional.of(orderWithItems);
    }

    @Override
    public void createOrder(Cart cart) {
        Order order = orderRepository.save(new Order(cart.getTotal()));
        cart.getItems().forEach(item -> orderItemRepository.save(new OrderItem(order.getId(), item.getId(), item.getCount())));
        cart.getItems().forEach(item -> {
            item.setCount(0);
            itemsRepository.save(item);
        });
        cart.setItems(new HashSet<>());
        cartRepository.save(cart);
    }
}
