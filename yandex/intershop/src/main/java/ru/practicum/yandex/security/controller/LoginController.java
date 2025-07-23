package ru.practicum.yandex.security.controller;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
public class LoginController {
    @GetMapping("/login")
    public Mono<String> login(ServerWebExchange exchange, Model model) {
        return Mono.just("login");
    }
}
