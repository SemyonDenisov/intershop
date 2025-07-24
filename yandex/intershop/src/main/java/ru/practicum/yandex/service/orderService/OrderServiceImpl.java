package ru.practicum.yandex.service.orderService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.dao.*;
import ru.practicum.yandex.dto.OrderWithItems;
import ru.practicum.yandex.model.*;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.security.model.User;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.paymentService.PaymentService;
import ru.practicum.yandex.service.cache.itemCacheService.ItemCacheService;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentService paymentService;
    private final ItemCacheService itemCacheService;
    private final UserRepository userRepository;
    private final CartService cartService;


    @Override
    public Flux<OrderWithItems> findAll(String username) {
        return orderRepository.findAll()
                .flatMap(order -> findById(order.getId(), username));
    }

    @Override
    public Mono<OrderWithItems> findById(int id, String username) {
        return userRepository.findByUsername(username).map(User::getId)
                .flatMap(userId -> orderRepository.findByIdAndUserId(id, userId))
                .doOnNext(order -> {
                    if (order == null) {
                        throw new RuntimeException("Order not found");
                    }
                })
                .flatMap(order -> orderItemRepository.findAllByOrderId(order.getId())
                        .flatMap(orderItem -> itemsRepository.findById(orderItem.getItemId())
                                .map(item -> {
                                    item.setCount(orderItem.getQuantity());
                                    return item;
                                })).collectList()
                        .map(items -> new OrderWithItems(order.getId(), items, order.getTotalSum())));
    }

    @Override
    public Mono<Order> createOrder(String username) {
        return cartService.getCartByUsername(username)
                .flatMap(cart1 -> {
                    Order newOrder = new Order(cart1.getTotal(), cart1.getUserId());
                    return orderRepository.save(newOrder)
                            .flatMap(order -> cartItemRepository.findByCartId(cart1.getId())
                                    .flatMap(cartItem -> itemsRepository.findById(cartItem.getItemId())
                                            .flatMap(item -> {
                                                int count = item.getCount();
                                                item.setCount(0);
                                                return Mono.zip(
                                                        orderItemRepository.save(new OrderItem(order.getId(), item.getId(), count)),
                                                        itemsRepository.save(item),
                                                        cartItemRepository.deleteByItemId(cartItem.getItemId())
                                                );
                                            })).collectList()
                                    .thenReturn(order)
                            )
                            .flatMap(order -> paymentService.makeOrder(order)
                                    .doOnNext(orderMake -> itemCacheService.deleteLists())
                                    .thenReturn(order));
                });

    }
}
