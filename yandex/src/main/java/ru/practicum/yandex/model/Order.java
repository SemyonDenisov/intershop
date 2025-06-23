package ru.practicum.yandex.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "orders")
public record Order(
        @Id
        int id,
        @OneToMany()
        List<Item> items,
        @Column
        double totalSum
) {
}
