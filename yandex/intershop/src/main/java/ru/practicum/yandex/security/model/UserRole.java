package ru.practicum.yandex.security.model;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name="user_role")
public class UserRole {

    @Id
    int id;

    @Column(value = "user_id")
    int userId;

    @Column(value = "role_id")
    int roleId;
}
