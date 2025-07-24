package ru.practicum.yandex.security.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerHttpBasicAuthenticationConverter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.WebSessionServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.server.WebSession;
import ru.practicum.yandex.security.dao.RoleRepository;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.security.dao.UserRoleRepository;
import ru.practicum.yandex.security.service.R2dbcUserDetailsManager;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveAuthenticationManager authenticationManager) {
        AuthenticationWebFilter authFilter = new AuthenticationWebFilter(authenticationManager);
        authFilter.setServerAuthenticationConverter(new ServerHttpBasicAuthenticationConverter());
        WebSessionServerCsrfTokenRepository csrfTokenRepository = new WebSessionServerCsrfTokenRepository();
        var csrfHandler = new XorServerCsrfTokenRequestAttributeHandler();

        csrfHandler.setTokenFromMultipartDataEnabled( true);

        return http
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(csrfHandler)
                        .csrfTokenRepository(csrfTokenRepository)

                )
                .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/main/items/*").hasRole("USER")
                        .pathMatchers("/main/**").permitAll()
                        .pathMatchers("/login").permitAll()
                        .anyExchange().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                )
                .logout(logoutSpec ->
                        logoutSpec
                                .logoutUrl("/logout")
                                .logoutHandler((exchange, authentication) -> exchange.getExchange().getSession().flatMap(WebSession::invalidate))
                                .logoutSuccessHandler((exchange, authentication) ->{
                                    exchange.getExchange().getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                                    exchange.getExchange().getResponse().getHeaders().setLocation(URI.create("/main/items"));
                                    return exchange.getExchange().getResponse().setComplete();
                                        }))
                .securityContextRepository(new WebSessionServerSecurityContextRepository())
                .build();
    }

    @Bean
    ReactiveUserDetailsService userDetailsService(UserRepository userRepository,
                                                  UserRoleRepository userRoleRepository,
                                                  RoleRepository roleRepository) {
        return new R2dbcUserDetailsManager(userRepository, userRoleRepository, roleRepository);
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
