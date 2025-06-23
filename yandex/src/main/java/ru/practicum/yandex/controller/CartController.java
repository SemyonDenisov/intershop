package ru.practicum.yandex.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;

import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ItemService itemService;

    public CartController(CartService cartService, ItemService itemService) {
        this.cartService = cartService;
        this.itemService = itemService;
    }

    @GetMapping(value = "/items")
    public String showCart(Model model) {
        Cart cart = cartService.getCartById(1).orElse(new Cart());
        model.addAttribute("items", cart.getItems());
        model.addAttribute("empty", cart.getItems().isEmpty());
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        cart.getItems()
                .forEach(item -> total.updateAndGet(v -> v + item.getPrice() * item.getCount()));
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping(value = "/items/{id}")
    public String addToCart(@PathVariable(name = "id") Integer id, @RequestParam(name = "action") String action, Model model) {
        if (action != null) {
            cartService.changeCart(id, action);
        }
        return "redirect:/cart/items";
    }
}
