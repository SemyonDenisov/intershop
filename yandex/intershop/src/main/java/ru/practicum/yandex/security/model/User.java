package ru.practicum.yandex.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Table(name = "users")
public class User {

    @Id
    int id;

    @Column(value = "username")
    String username;
    @Column(value = "password")
    String password;

    @Column("cart_id")
    Integer cartId;


    @Setter
    @Transient
    List<Role> roles;
}
