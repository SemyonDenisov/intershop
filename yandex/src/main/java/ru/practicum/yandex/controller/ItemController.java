package ru.practicum.yandex.controller;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;




@Controller
public class ItemController {

    private final ItemService itemService;
    private final CartService cartService;

    public ItemController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }


    @GetMapping(value = "/items/{id}")
    public String getItemInfo(@PathVariable Integer id, Model model) {
        itemService.findById(id).ifPresentOrElse(item -> {
            model.addAttribute("item", item);
        }, RuntimeException::new);
        return "item";
    }

    @PostMapping(value = "/items/{id}")
    public String addItemToCart(@PathVariable(name = "id") Integer id,
                                @RequestParam(name = "action") String action) {
        if (action != null) {
            cartService.changeCart(id, action);
        }
        return "redirect:/items/" + id;
    }

    @GetMapping(value = "/items/add-form")
    public String addItemForm() {
        return "add-item";
    }

    @PostMapping(value = "/items/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addItem(@RequestParam(name = "title") String title,
                           @RequestPart(name = "image") MultipartFile image,
                           @RequestParam(name = "price") Double price,
                           @RequestParam(name = "description") String description) {
        itemService.addItem(title,description,price,image);
        return "redirect:/main/items";
    }

}
