package ru.practicum.yandex.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import ru.practicum.yandex.security.dao.RoleRepository;
import ru.practicum.yandex.security.dao.UserRepository;
import ru.practicum.yandex.security.dao.UserRoleRepository;


@AllArgsConstructor
public class R2dbcUserDetailsManager implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> userRoleRepository
                        .findByUserId(user.getId())
                        .flatMap(userRole -> roleRepository.findById(userRole.getRoleId()))
                        .collectList().flatMap(roles -> {
                            user.setRoles(roles);
                            return Mono.just(user);
                        })
                ).map(userWithRoles -> new org.springframework.security.core.userdetails.User(
                        userWithRoles.getUsername(), userWithRoles.getPassword(), userWithRoles.getRoles()
                ));
    }
}
