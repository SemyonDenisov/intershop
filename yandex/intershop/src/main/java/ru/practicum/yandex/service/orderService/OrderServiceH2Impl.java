package ru.practicum.yandex.service.orderService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.DAO.*;
import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.model.*;
import ru.practicum.yandex.service.paymentService.PaymentService;
import ru.practicum.yandex.service.cache.itemCacheService.ItemCacheService;

import java.util.List;

@Service
public class OrderServiceH2Impl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentService paymentService;
    private final ItemCacheService itemCacheService;

    public OrderServiceH2Impl(OrderRepository orderRepository,
                              CartRepository cartRepository,
                              ItemsRepository itemsRepository,
                              OrderItemRepository orderItemRepository,
                              CartItemRepository cartItemRepository,
                              PaymentService paymentService,
                              ItemCacheService itemCacheService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.itemsRepository = itemsRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.paymentService = paymentService;
        this.itemCacheService = itemCacheService;
    }


    @Override
    public Flux<OrderWithItems> findAll() {
        return orderRepository.findAll().flatMap(order -> findById(order.getId()));
    }

    @Override
    public Mono<OrderWithItems> findById(int id) {
        return orderItemRepository.findAllByOrderId(id)
                .flatMap(orderItem -> itemsRepository.findById(orderItem.getItemId())
                        .map(item -> {
                            item.setCount(orderItem.getQuantity());
                            return item;
                        }))
                .collectList()
                .zipWith(orderRepository.findById(id))
                .flatMap(tuple -> {
                    List<Item> items = tuple.getT1();
                    Order order = tuple.getT2();
                    return Mono.just(new OrderWithItems(order.getId(), items, order.getTotalSum()));
                });
    }

    @Override
    public Mono<Order> createOrder(Cart cart) {
        return orderRepository.save(new Order(cart.getTotal()))
                .flatMap(order ->
                        cartItemRepository.findByCartId(cart.getId())
                                .flatMap(cartItem ->
                                        itemsRepository.findById(cartItem.getItemId())
                                                .flatMap(item -> {
                                                    int count = item.getCount();
                                                    item.setCount(0);

                                                    return Mono.zip(
                                                            orderItemRepository.save(new OrderItem(order.getId(), item.getId(), count)),
                                                            itemsRepository.save(item),
                                                            cartItemRepository.deleteByItemId(cartItem.getItemId())
                                                    );
                                                })
                                )
                                .then(paymentService.makeOrder(order))
                                .doOnNext(orderMake -> itemCacheService.deleteLists())
                                .then(Mono.just(order))
                );
    }
}
