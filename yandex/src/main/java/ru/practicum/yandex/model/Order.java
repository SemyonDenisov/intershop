package ru.practicum.yandex.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "total_sum")
    double totalSum;

    public Order(double totalSum) {
        this.totalSum = totalSum;
    }

    public Order() {
    }

}
