package ru.practicum.yandex.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
public class CsrfWebFilterConfig {

    @Bean
    public WebFilter csrfTokenAddingWebFilter() {
        return (exchange, chain) -> exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty())
                .switchIfEmpty(Mono.defer(() -> exchange.getSession().thenReturn(null))) // Force session creation
                .flatMap(csrfToken -> {
                    exchange.getAttributes().put("_csrf", csrfToken);
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}
