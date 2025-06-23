package ru.practicum.yandex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

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
