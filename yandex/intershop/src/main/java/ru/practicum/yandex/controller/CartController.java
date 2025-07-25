package ru.practicum.yandex.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.service.paymentService.PaymentService;
import ru.practicum.yandex.service.cartService.CartService;

import java.security.Principal;


@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;


    @GetMapping(value = "/items")
    @PreAuthorize("hasRole('USER')")
    public Mono<String> showCart(Model model, Principal principal) {
        return cartService.getCartByUsername(principal.getName())
                .flatMap(cart ->
                        paymentService
                                .getBalance(principal.getName())
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
    @PreAuthorize("hasRole('USER')")
    public Mono<String> addToCart(@PathVariable(name = "id") Integer id, @RequestPart(name = "action") String action, Principal principal) {
        return cartService.changeCart(id, action, principal.getName()).thenReturn("redirect:/cart/items");
    }
}
