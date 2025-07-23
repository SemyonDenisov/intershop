package ru.practicum.yandex.security.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.security.model.Role;
import ru.practicum.yandex.security.model.User;

public interface RoleRepository extends R2dbcRepository<Role, Integer> {
    Mono<Role> findById(int id);
}
