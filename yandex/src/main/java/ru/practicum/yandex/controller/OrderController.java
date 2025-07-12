package ru.practicum.yandex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.orderService.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping
    public Mono<String> orders(Model model) {
        return orderService.findAll().collectList()
                .flatMap(orderWithItems -> {
                    model.addAttribute("orders", orderWithItems);
                    return Mono.just("orders");
                });
    }

    @GetMapping("/{id}")
    public Mono<String> order(@PathVariable(name = "id") int id,
                              @RequestParam(name = "newOrder",defaultValue = "false") Boolean newOrder,
                              Model model) {
        return orderService.findById(id).flatMap(order -> {
            model.addAttribute("order", order);
            model.addAttribute("newOrder", newOrder);
            return Mono.just("order");
        });

    }

    @PostMapping(value = "/buy")
    public Mono<String> buy() {
        return cartService.getCartById(1)
                .flatMap(orderService::createOrder)
                .flatMap(order -> Mono.just("redirect:/orders/" + order.getId() + "?newOrder=true"));
    }
}
