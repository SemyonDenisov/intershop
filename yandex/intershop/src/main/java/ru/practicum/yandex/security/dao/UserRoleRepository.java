package ru.practicum.yandex.security.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.practicum.yandex.security.model.User;
import ru.practicum.yandex.security.model.UserRole;

public interface UserRoleRepository extends R2dbcRepository<UserRole, Integer> {
    Flux<UserRole> findByUserId(int userId);
}
