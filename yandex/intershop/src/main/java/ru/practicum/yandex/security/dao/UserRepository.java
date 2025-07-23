package ru.practicum.yandex.security.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.security.model.User;

public interface UserRepository extends R2dbcRepository<User, Integer> {
    Mono<User> findByUsername(String username);
}
