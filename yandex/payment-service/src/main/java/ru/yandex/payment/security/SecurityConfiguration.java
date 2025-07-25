package ru.yandex.payment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated()
                ).oauth2ResourceServer(serverSpec ->
                        serverSpec
                                .jwt(jwtSpec -> {
                                    ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
                                    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                                        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
                                        if (resourceAccess != null) {
                                            Map<String, Object> account = (Map<String, Object>) resourceAccess.get("account");
                                            if (account != null) {
                                                List<String> roles = (List<String>) account.get("roles");
                                                if (roles != null) {
                                                    return Flux.fromIterable(roles)
                                                            .map(SimpleGrantedAuthority::new)
                                                            .map(GrantedAuthority.class::cast);
                                                }
                                            }
                                        }
                                        return Flux.empty();

                                    });

                                    jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter);
                                })
                );

        return http.build();
    }

}