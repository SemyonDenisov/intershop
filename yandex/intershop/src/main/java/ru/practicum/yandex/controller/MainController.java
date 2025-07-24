package ru.practicum.yandex.controller;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.model.Item;
import ru.practicum.yandex.paging.Paging;
import ru.practicum.yandex.service.cartService.CartService;
import ru.practicum.yandex.service.itemService.ItemService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;


@Controller
public class MainController {

    private final ItemService itemService;
    private final CartService cartService;

    public MainController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping(value = "/")
    public Mono<String> index() {
        return Mono.just("redirect:/main/items");
    }

    @GetMapping(value = "/main/items")
    public Mono<String> items(Model model,
                              @RequestParam(name = "search", defaultValue = "") String search,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber,
                              @RequestParam(name = "sort", defaultValue = "NO") String sort) {

        Flux<Item> items = itemService.findAll(pageSize, pageNumber, search, sort);
        Mono<Long> totalItems = itemService.getCount();
        return items.collectList().zipWith(totalItems).flatMap(tuple -> {
            List<Item> items1 = tuple.getT1();
            long count = tuple.getT2();
            Paging paging = new Paging(pageNumber, pageSize, count / ((long) pageNumber * pageSize) > 0, pageNumber > 1);
            model.addAttribute("items", Lists.partition(items1, 5));
            model.addAttribute("search", search);
            model.addAttribute("sort", sort);
            model.addAttribute("paging", paging);
            return Mono.just("main");
        });
    }

    @PostMapping(value = "/main/items/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<String> changeCountOfItemInCart(@PathVariable(name = "id") Integer id,
                                                @RequestPart(name = "action") String action,
                                                Principal principal) {
        return cartService.changeCart(id, action, principal.getName()).thenReturn("redirect:/main/items");
    }

    @GetMapping(value = "/main/image/{filename}")
    @ResponseBody
    public byte[] image(@PathVariable String filename) throws IOException {
        return itemService.getImage(filename);
    }

}
