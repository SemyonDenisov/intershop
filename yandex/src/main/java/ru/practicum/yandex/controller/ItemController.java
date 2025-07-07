package ru.practicum.yandex.controller;


import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
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
    public Mono<String> getItemInfo(@PathVariable Integer id, Model model) {
        return itemService.findById(id).map(item -> {
            model.addAttribute("item", item);
            return "item";
        });
    }

    @PostMapping(value = "/items/{id}")
    public Mono<String> addItemToCart(@PathVariable(name = "id") Integer id,
                                @RequestParam(name = "action") String action) {
        return cartService.changeCart(id,action).map(res-> "redirect:/items/" + id);
    }

    @GetMapping(value = "/items/add-form")
    public String addItemForm() {
        return "add-item";
    }

    @PostMapping(value = "/items/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<String> addItem(@RequestParam(name = "title") String title,
                           @RequestPart(name = "image") Mono<FilePart> image,
                           @RequestParam(name = "price") Double price,
                           @RequestParam(name = "description") String description) {
        return itemService.addItem(title,description,price,image).map(item-> "redirect:/main/items/");
    }

}
