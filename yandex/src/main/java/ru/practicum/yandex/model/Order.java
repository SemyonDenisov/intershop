package ru.practicum.yandex.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    int id;

    @Column("total_sum")
    double totalSum;

    public Order(double totalSum) {
        this.totalSum = totalSum;
    }

    public Order() {
    }

}
