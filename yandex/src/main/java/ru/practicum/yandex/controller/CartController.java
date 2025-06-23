package ru.practicum.yandex.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

    private final CartService cartService;
    private final ItemService itemService;

    public CartController(CartService cartService, ItemService itemService) {
        this.cartService = cartService;
        this.itemService = itemService;
    }

    @GetMapping(value = "/items")
    public String showCart(Model model) {
        cartService.getCartById(1).ifPresent((cart) -> {
            cart.getItems().stream().map(Item::getPrice).reduce(Double::sum)
                    .ifPresent(total -> {
                                model.addAttribute("total", total);
                            }
                    );
            model.addAttribute("items", cart.getItems());
            model.addAttribute("empty", cart.getItems().isEmpty());
        });
        return "cart";
    }

    @PostMapping(value = "/items/{id}")
    public String addToCart(@PathVariable(name = "id") Integer id, @RequestParam(name = "action") String action, Model model) {
        cartService.addToCart(id, action);
        return "redirect:/cart/items";
    }
}
