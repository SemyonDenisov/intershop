package ru.practicum.yandex.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.model.Cart;
import ru.practicum.yandex.service.cartService.CartService;


@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;


    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(value = "/items")
    public String showCart(Model model) {
        Cart cart = cartService.getCartById(1).orElse(new Cart());
        model.addAttribute("items", cart.getItems());
        model.addAttribute("empty", cart.getItems().isEmpty());
        model.addAttribute("total", cart.getTotal());
        return "cart";
    }

    @PostMapping(value = "/items/{id}")
    public String addToCart(@PathVariable(name = "id") Integer id, @RequestParam(name = "action") String action) {
        if (action != null) {
            cartService.changeCart(id, action);
        }
        return "redirect:/cart/items";
    }
}
