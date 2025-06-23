package ru.practicum.yandex.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.DAO.CartRepository;
import ru.practicum.yandex.DAO.ItemsRepository;
import ru.practicum.yandex.model.Item;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;

    public CartController(CartRepository cartRepository, ItemsRepository itemsRepository) {
        this.cartRepository = cartRepository;
        this.itemsRepository = itemsRepository;
    }

    @GetMapping(value = "/items")
    public String showCart(Model model) {

        cartRepository.findById(1).ifPresent((cart) -> {
            cart.getItems().stream().map(Item::getPrice).reduce(Double::sum)
                    .ifPresent(total -> {
                                model.addAttribute("total", total);
                            }
                    );
            model.addAttribute("items", cart.getItems());
            model.addAttribute("empty",cart.getItems().isEmpty());
        });
        return "cart";
    }

    @PostMapping(value = "/items/{id}")
    public String addToCart(@PathVariable(name = "id") Integer id,@RequestParam(name="action") Model model) {
        cartRepository.findById(1).ifPresent((cart) -> {
        });
        return "redirect:/cart/items";
    }
}
