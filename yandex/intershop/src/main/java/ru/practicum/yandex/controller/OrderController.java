package ru.practicum.yandex.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.orderService.OrderService;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    public Mono<String> orders(Model model, Principal principal) {
        return orderService.findAll(principal.getName()).collectList()
                .flatMap(orderWithItems -> {
                    model.addAttribute("orders", orderWithItems);
                    return Mono.just("orders");
                });
    }

    @GetMapping("/{id}")
    public Mono<String> order(@PathVariable(name = "id") int id,
                              @RequestParam(name = "newOrder", defaultValue = "false") Boolean newOrder,
                              Model model, Principal principal) {
        return orderService.findById(id, principal.getName()).flatMap(order -> {
            model.addAttribute("order", order);
            model.addAttribute("newOrder", newOrder);
            return Mono.just("order");
        });

    }

    @PostMapping(value = "/buy")
    public Mono<String> buy(Principal principal) {
        return orderService.createOrder(principal.getName())
                .flatMap(order -> Mono.just("redirect:/orders/" + order.getId() + "?newOrder=true"))
                .onErrorResume(error -> Mono.just("error"));
    }
}
