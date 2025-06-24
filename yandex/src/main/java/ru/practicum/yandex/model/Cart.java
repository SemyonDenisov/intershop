package ru.practicum.yandex.model;

import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@Table(name="cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToMany
    @JoinTable(
            name = "cart_items",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    Set<Item> items;

    public Cart() {
        id=1;
        items = new HashSet<>();
    }

    public int getId() {
        return id;
    }
    public Set<Item> getItems() {
        return items;
    }
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public double getTotal() {
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        this.getItems()
                .forEach(item -> total.updateAndGet(v -> v + item.getPrice() * item.getCount()));
        return total.get();
    }
}
