package ru.practicum.yandex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.DTO.OrderWithItems;
import ru.practicum.yandex.model.Order;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.orderService.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController    {
    private final OrderService orderService;
    private final CartService cartService;
    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping
    public String orders(Model model) {

        model.addAttribute("orders", orderService.findAll());
        return "orders";
    }

    @GetMapping("/{id}")
    public String order(@PathVariable(name="id") int id,
                        @RequestParam(defaultValue = "false") boolean newOrder,
                        Model model) {
        OrderWithItems order = orderService.findById(id).orElseThrow();
        model.addAttribute("order",order);
        model.addAttribute("newOrder",newOrder);
        return "order";
    }

    @PostMapping(value = "/buy")
    public String buy() {
        cartService.getCartById(1).ifPresent(orderService::createOrder);
        return "redirect:/orders?newOrder=true";
    }
}
