package ru.practicum.yandex.controller;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.service.paymentService.PaymentService;
import ru.practicum.yandex.service.cartService.CartService;


@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final PaymentService paymentService;


    public CartController(CartService cartService,
                          PaymentService paymentService) {
        this.cartService = cartService;
        this.paymentService = paymentService;
    }

    @GetMapping(value = "/items")
    public Mono<String> showCart(Model model) {
        return cartService.getCartById(1)
                .flatMap(cart ->
                        paymentService
                                .getBalance()
                                .onErrorResume(throwable -> Mono.just(-1.0))
                                .flatMap(balance -> {
                                    if (balance < 0) {
                                        model.addAttribute("serverAvailable", false);
                                    } else {
                                        model.addAttribute("serverAvailable", true);
                                    }
                                    model.addAttribute("availableToBuy", balance >= cart.getTotal());
                                    model.addAttribute("items", cart.getItems());
                                    model.addAttribute("empty", cart.getItems().isEmpty());
                                    model.addAttribute("total", cart.getTotal());
                                    return Mono.just("cart");
                                }));
    }

    @PostMapping(value = "/items/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<String> addToCart(@PathVariable(name = "id") Integer id, @RequestPart(name = "action") String action) {
        return cartService.changeCart(id, action).thenReturn("redirect:/cart/items");
    }
}
