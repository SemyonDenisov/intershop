package ru.practicum.yandex.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.Setter;


@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    int id;

    @Column(value = "user_id")
    int userId;

    @Column("total_sum")
    double totalSum;

    public Order(double totalSum,Integer userId) {
        this.totalSum = totalSum;
        this.userId = userId;
    }

    public Order() {
    }

}
