package ru.practicum.yandex.controller;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.paging.Paging;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;


@Controller
public class MainController {

    private final ItemService itemService;
    private final CartService cartService;

    public MainController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping(value = "/")
    public String index(Model model) {
        return "redirect:/main/items";
    }

    @GetMapping(value = "/main/items")
    public String items(Model model,
                        @RequestParam(name = "search", defaultValue = "") String search,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber,
                        @RequestParam(name = "sort", defaultValue = "NO") String sort) {
        Sort sortForRequest;
        switch (sort) {
            case "AlPHA" -> sortForRequest = Sort.by(Sort.Direction.ASC, "title");
            case "PRICE" -> sortForRequest = Sort.by(Sort.Direction.ASC, "price");
            default -> sortForRequest = Sort.by(Sort.Direction.DESC, "id");
        }
        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize,sortForRequest);
        Page<Item> items = itemService.findAll(pageRequest,search);
        Paging paging = new Paging(pageNumber, pageSize, items.getTotalPages() > pageNumber, pageNumber > 1);
        model.addAttribute("items", Lists.partition(items.getContent(), 5));
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("paging", paging);
        return "main";
    }

    @PostMapping(value = "/main/items/{id}")
    public String changeCountOfItemInCart(@PathVariable(name="id") Integer id, @RequestParam String action, Model model) {
        if (action != null) {
            cartService.changeCart(id,action);
        }
        return "redirect:/main/items";
    }

}
