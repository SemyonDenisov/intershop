package ru.practicum.yandex.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Table(name="cart")
@AllArgsConstructor
public class Cart {
    @Id
    int id;

    @Setter
    @Transient
    Set<Item> items;

    public Cart() {
    }

    public double getTotal() {
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        if(items == null) {
            return 0.0;
        }
        this.getItems()
                .forEach(item -> total.updateAndGet(v -> v + item.getPrice() * item.getCount()));
        return total.get();
    }
}
