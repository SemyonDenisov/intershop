//package ru.practicum.yandex.controller;
//
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import ru.practicum.yandex.model.Item;
//import ru.practicum.yandex.model.Order;
//import ru.practicum.yandex.service.cartService.CartService;
//import ru.practicum.yandex.service.itemService.ItemService;
//
//import java.util.concurrent.atomic.AtomicReference;
//
//
//@Controller
//public class ItemController {
//
//    private final ItemService itemService;
//    private final CartService cartService;
//
//    public ItemController(ItemService itemService, CartService cartService) {
//        this.itemService = itemService;
//        this.cartService = cartService;
//    }
//
//
//    @GetMapping(value = "/items/{id}")
//    public String getItemInfo(@PathVariable Integer id, Model model) {
//        itemService.findById(id).ifPresentOrElse(item -> {
//            model.addAttribute("item", item);
//        }, RuntimeException::new);
//        return "item";
//    }
//
//    @PostMapping(value = "/items/{id}")
//    public String addItemToCart(@PathVariable Integer id, Model model) {
//        return "redirect:/items/" + id;
//    }
//
//    @PostMapping(value = "/buy")
//    public String buy() {
//        cartService.getCartById(1).ifPresent(cart -> {
//            AtomicReference<Double> total = new AtomicReference<>(0.0);
//            cart.getItems()
//                    .stream().map(Item::getPrice)
//                    .reduce(Double::sum).ifPresent(total::set);
//            new Order(1, cart.getItems(), total.get());
//        });
//        return "redirect:/orders?newOrder=true";
//    }
//
//}
