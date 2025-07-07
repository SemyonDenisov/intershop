package ru.practicum.yandex.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.service.cartService.CartService;


@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;


    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(value = "/items")
    public Mono<String> showCart(Model model) {
        return cartService.getCartById(1).handle((cart, sink) -> {
            if (cart == null) {
                sink.error(new RuntimeException("Cart is null"));
                return;
            }
            model.addAttribute("items", cart.getItems());
            model.addAttribute("empty", cart.getItems().isEmpty());
            model.addAttribute("total", cart.getTotal());
            sink.next("cart");
        });
    }

    @PostMapping(value = "/items/{id}")
    public Mono<String> addToCart(@PathVariable(name = "id") Integer id, @RequestParam(name = "action") String action) {
        return cartService.changeCart(id, action).map(res -> "redirect:/cart/items");
    }
}
